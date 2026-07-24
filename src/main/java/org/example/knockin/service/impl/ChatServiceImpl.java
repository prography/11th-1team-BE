package org.example.knockin.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.ChatMessageDto;
import org.example.knockin.dto.ChatRoomCreateDto;
import org.example.knockin.dto.ChatRoomDetailDto;
import org.example.knockin.dto.ChatRoomDto;
import org.example.knockin.dto.ChatRoomDto.Response;
import org.example.knockin.dto.ChatRoomImageDto;
import org.example.knockin.dto.ChatRoomListDto;
import org.example.knockin.dto.ChatRoomLeftEvent;
import org.example.knockin.dto.ChatRoomMessageEvent;
import org.example.knockin.dto.ChatSocketResponse;
import org.example.knockin.dto.EventType;
import org.example.knockin.dto.MessageType;
import org.example.knockin.dto.RoommateRequestDto.RoommateMatchingRequiredInfo;
import org.example.knockin.entity.board.RoommateBoard;
import org.example.knockin.entity.chat.ChatRoomMember;
import org.example.knockin.entity.chat.ChatRoomMessage;
import org.example.knockin.entity.chat.ChattingRequired;
import org.example.knockin.entity.chat.ChattingRoom;
import org.example.knockin.entity.file.File;
import org.example.knockin.entity.file.FileType;
import org.example.knockin.entity.member.Member;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.ChattingErrorCode;
import org.example.knockin.exception.FileErrorCode;
import org.example.knockin.global.util.DateUtils;
import org.example.knockin.repository.member.row.ChattingRoomBasicInfoRow;
import org.example.knockin.service.FileService;
import org.example.knockin.service.RoommateScoreService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {
    private static final String ROOM_LEAVE_MESSAGE_CONTENTS = "상대방이 나갔습니다.";
    private static final String IMAGE_MESSAGE_CONTENTS = "사진을 보냈습니다.";

    private final ChattingRoomServiceImpl chattingRoomService;
    private final ChatRoomMemberServiceImpl chatRoomMemberService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ApplicationEventPublisher publisher;
    private final ChatRoomMessageServiceImpl chatRoomMessageService;
    private final FileService fileService;
    private final ChatRoomFileServiceImpl chatRoomFileService;
    private final BasicInformationServiceImpl basicInformationService;
    private final RoommateMatchingRequiredServiceImpl roommateMatchingRequiredService;
    private final RoommateBoardServiceImpl roommateBoardService;
    private final ChattingRequiredServiceImpl chattingRequiredService;
    private final MemberServiceImpl memberService;
    private final RoommateScoreService roommateScoreService;
    private final ChattingScoreServiceImpl chattingScoreService;
    @Value("${policy.chat.room-limit-per-member}")
    private long chatRoomLimitPerMember;

    public List<ChatRoomListDto.Response> getChatRoomList(Long memberId) {
        return chattingRoomService.findByMemberId(memberId);
    }

    @Transactional
    public ChatRoomImageDto.Response uploadImage(Long chatRoomId, Long memberId, MultipartFile multipartFile) {
        validateImageFile(multipartFile);
        chatRoomMemberService.checkCanSendMessage(chatRoomId, memberId);

        try {
            File savedFile = fileService.save(multipartFile, FileType.CHAT_ROOM_IMAGE);
            return new ChatRoomImageDto.Response(savedFile.getSavedFileName());
        } catch (IOException e) {
            throw new BusinessException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public ChatRoomDto.Response leaveChatRoom(Long memberId, Long chatRoomId) {
        ChatRoomMember roomMember = chatRoomMemberService.findActiveMemberByRoomIdAndMemberId(chatRoomId, memberId);
        roomMember.left();

        ChattingRoom chattingRoom = chattingRoomService.findByIdOrThrow(chatRoomId);
        chatRoomMessageService.save(ROOM_LEAVE_MESSAGE_CONTENTS, null, chattingRoom, MessageType.LEFT_ROOM);

        LocalDateTime now = LocalDateTime.now();
        publisher.publishEvent(new ChatRoomLeftEvent(chatRoomId, now, ROOM_LEAVE_MESSAGE_CONTENTS));
        return new Response(now);
    }

    @Transactional
    public void sendUserMessage(Long chatRoomId, ChatMessageDto.Request request, Long senderId) {
        validateMessageRequest(request);
        ChatRoomMember chatRoomMember = chatRoomMemberService.findActiveMemberByRoomIdAndMemberId(chatRoomId, senderId);
        ChattingRoom chattingRoom = chattingRoomService.findByIdOrThrow(chatRoomId);
        Member member = chatRoomMember.getMember();
        MessageType type = request.getType();

        switch (request.getType()) {
            case TEXT -> {
                chatRoomMessageService.save(request.getMessage(), member, chattingRoom, type);
                publishMessageEvent(chatRoomId, senderId, request);
            }
            case IMAGE -> {
                ChatRoomMessage chatRoomMessage = chatRoomMessageService.save(IMAGE_MESSAGE_CONTENTS, member, chattingRoom, type);
                File file = fileService.findBySavedFileNameAndType(request.getImageUrl(), FileType.CHAT_ROOM_IMAGE);
                chatRoomFileService.save(file, chatRoomMessage);
                publishMessageEvent(chatRoomId, senderId, request);
            }
            default -> throw new BusinessException(ChattingErrorCode.MESSAGE_PAYLOAD_INVALID);
        }
    }

    private void publishMessageEvent(Long chatRoomId, Long senderId, ChatMessageDto.Request request) {
        ChatRoomMessageEvent event = ChatRoomMessageEvent.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .clientMessageId(request.getClientMessageId())
                .messageType(request.getType())
                .message(request.getMessage())
                .imageUrl(request.getImageUrl())
                .build();

        publisher.publishEvent(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatRoomLeft(ChatRoomLeftEvent event) {
        ChatSocketResponse<ChatMessageDto.Response> response = ChatSocketResponse.of(
                EventType.SYSTEM_MESSAGE,
                event.chatRoomId(),
                ChatMessageDto.Response.userLeft(event),
                event.leftAt()
        );
        messagingTemplate.convertAndSend("/sub/chats/" + event.chatRoomId(), response);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageSend(ChatRoomMessageEvent event) {
        ChatSocketResponse<ChatMessageDto.Response> response = ChatSocketResponse.of(
                EventType.USER_MESSAGE,
                event.chatRoomId(),
                ChatMessageDto.Response.chatMessage(event)
        );
        messagingTemplate.convertAndSend("/sub/chats/" + event.chatRoomId(), response);
    }

    private void validateMessageRequest(ChatMessageDto.Request request) {
        if (request == null
                || !StringUtils.hasText(request.getClientMessageId())
                || request.getType() == null) {
            throw new BusinessException(ChattingErrorCode.MESSAGE_PAYLOAD_INVALID);
        }

        switch (request.getType()) {
            case TEXT -> validateTextMessage(request);
            case IMAGE -> validateImageMessage(request);
        }
    }

    @Transactional
    public ChatRoomDetailDto.Response getChatRoomDetail(Long chatRoomId, Long memberId) {
        ChattingRoom chattingRoom = chattingRoomService.findByIdOrThrow(chatRoomId);
        ChatRoomMember chatRoomMember = chatRoomMemberService.findActiveMemberByRoomIdAndMemberId(chatRoomId, memberId);

        ChatRoomDetailDto.ProfileInfo opponentProfile = getOpponentProfileInfo(chatRoomMember, chatRoomId);
        List<ChatRoomDetailDto.ChatMessage> messages = chatRoomMessageService.findChatMessageDto(chatRoomId);
        List<RoommateMatchingRequiredInfo> matchingRequiredList = roommateMatchingRequiredService.findRequiredDto(chattingRoom);

        return ChatRoomDetailDto.Response.builder()
                .opponentProfile(opponentProfile)
                .messages(messages)
                .matchingRequiredList(matchingRequiredList)
                .build();
    }

    private ChatRoomDetailDto.ProfileInfo getOpponentProfileInfo(ChatRoomMember me, Long chatRoomId) {
        Member opponentMember = chatRoomMemberService.findPartnerMember(me, chatRoomId);
        ChattingRoomBasicInfoRow row = basicInformationService.findChattingRoomBasicInfoRowByMemberId(opponentMember.getId());
        Integer score = roommateScoreService.calculateSimpleScore(me.getMember().getId(), opponentMember.getId());

        return ChatRoomDetailDto.ProfileInfo.builder()
                .id(row.memberId())
                .name(row.name())
                .age(DateUtils.calculateAge(row.birth()))
                .gender(row.gender())
                .memberProfileImageUrl(row.profileImageUrl())
                .score(score)
                .build();
    }

    @Transactional
    public ChatRoomCreateDto.Response createChattingRoom(Long requesterId, ChatRoomCreateDto.Request request) {
        Member requester = memberService.findByIdOrThrow(requesterId);
        Member requestee = memberService.findByIdOrThrow(request.getRequesteeId());

        validateActiveRoomDoesNotExist(requesterId, request.getRequesteeId());
        validateChatRoomLimit(requesterId, request.getRequesteeId());

        RoommateBoard roommateBoard = roommateBoardService.findById(request.getBoardId());
        ChattingRequired chattingRequired = chattingRequiredService.saveAccepted(requester, requestee, roommateBoard);
        ChattingRoom chattingRoom = chattingRoomService.save(chattingRequired);
        chatRoomMemberService.saveAll(chattingRoom, List.of(requester, requestee));
        String contents = request.getChatMessage().getContents();
        ChatRoomMessage chatRoomMessage = chatRoomMessageService.save(contents, requester, chattingRoom, MessageType.TEXT);
        chattingScoreService.saveAll(roommateScoreService.createChattingScores(chattingRequired));

        return ChatRoomCreateDto.Response.builder()
                .chatRoomId(chattingRoom.getId())
                .updatedAt(chatRoomMessage.getCreatedAt())
                .build();
    }

    @Transactional
    public ChatRoomCreateDto.Response getOrCreateChattingRoom(ChattingRequired chattingRequired) {
        // 수락 요청이 다시 들어와도 방을 하나 더 만들지 않고 기존 방을 돌려준다.
        return chattingRoomService.findByChattingRequired(chattingRequired)
                .map(room -> ChatRoomCreateDto.Response.builder()
                        .chatRoomId(room.getId())
                        .updatedAt(room.getCreatedAt())
                        .build())
                .orElseGet(() -> createChattingRoom(chattingRequired));
    }

    private ChatRoomCreateDto.Response createChattingRoom(ChattingRequired chattingRequired) {
        Member requester = chattingRequired.getRequester();
        Member requestee = chattingRequired.getRequestee();

        validateActiveRoomDoesNotExist(requester.getId(), requestee.getId());
        validateChatRoomLimit(requester.getId(), requestee.getId());

        ChattingRoom chattingRoom = chattingRoomService.save(chattingRequired);
        chatRoomMemberService.saveAll(chattingRoom, List.of(requester, requestee));
        chattingScoreService.saveAll(roommateScoreService.createChattingScores(chattingRequired));

        return ChatRoomCreateDto.Response.builder()
                .chatRoomId(chattingRoom.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void validateActiveRoomDoesNotExist(Long requesterId, Long requesteeId) {
        if (chattingRoomService.existsActiveRoomBetweenMembers(requesterId, requesteeId)) {
            throw new BusinessException(ChattingErrorCode.ROOM_DUPLICATE);
        }
    }

    private void validateChatRoomLimit(Long requesterId, Long requesteeId) {
        long requesterRoomCount = chattingRoomService.countActiveRoomsByMemberId(requesterId);
        long requesteeRoomCount = chattingRoomService.countActiveRoomsByMemberId(requesteeId);

        if (requesterRoomCount >= chatRoomLimitPerMember || requesteeRoomCount >= chatRoomLimitPerMember) {
            throw new BusinessException(ChattingErrorCode.ROOM_LIMIT_EXCEEDED);
        }
    }

    private void validateTextMessage(ChatMessageDto.Request request) {
        if (!StringUtils.hasText(request.getMessage())) {
            throw new BusinessException(ChattingErrorCode.MESSAGE_PAYLOAD_INVALID);
        }
    }

    private void validateImageMessage(ChatMessageDto.Request request) {
        if (!StringUtils.hasText(request.getImageUrl())) {
            throw new BusinessException(ChattingErrorCode.MESSAGE_PAYLOAD_INVALID);
        }
    }

    private void validateImageFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(FileErrorCode.FILE_EMPTY);
        }
    }
}
