package org.example.knockin.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.ChatRoomListDto;
import org.example.knockin.entity.chat.ChattingRequired;
import org.example.knockin.entity.chat.ChattingRoom;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.ChattingErrorCode;
import org.example.knockin.repository.chat.ChattingRoomRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRoomServiceImpl {

    private final ChattingRoomRepository chattingRoomRepository;

    public List<ChatRoomListDto.Response> findByMemberId(Long memberId) {
        return chattingRoomRepository.findByMemberId(memberId);
    }

    public ChattingRoom findByIdOrThrow(Long chatRoomId) {
        return chattingRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ChattingErrorCode.ROOM_NOT_FOUND));
    }

    public boolean existsActiveRoomBetweenMembers(Long requesterId, Long requesteeId) {
        return chattingRoomRepository.existsActiveRoomBetweenMembers(requesterId, requesteeId);
    }

    public long countActiveRoomsByMemberId(Long memberId) {
        return chattingRoomRepository.countActiveRoomsByMemberId(memberId);
    }

    public ChattingRoom save(ChattingRequired chattingRequired) {
        ChattingRoom chattingRoom = ChattingRoom.builder()
                .chattingRequired(chattingRequired)
                .build();
        return chattingRoomRepository.save(chattingRoom);
    }

    public Optional<ChattingRoom> findByChattingRequired(ChattingRequired chattingRequired) {
        return chattingRoomRepository.findByChattingRequired(chattingRequired);
    }
}
