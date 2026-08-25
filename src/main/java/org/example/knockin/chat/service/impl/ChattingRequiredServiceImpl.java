package org.example.knockin.chat.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.knockin.board.entity.RoommateBoard;
import org.example.knockin.chat.entity.ChattingRequired;
import org.example.knockin.chat.entity.ChattingRequiredStatus;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.RequiredErrorCode;
import org.example.knockin.chat.repository.ChattingRequiredRepository;
import org.example.knockin.chat.repository.row.ChatRequestListRow;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRequiredServiceImpl {

    private final ChattingRequiredRepository chattingRequiredRepository;

    public List<ChatRequestListRow> findAllPendingByRequestee(Member requestee) {
        return chattingRequiredRepository.findAllPendingByRequestee(requestee);
    }

    public ChattingRequired findByIdOrThrow(Long requestId) {
        return chattingRequiredRepository.findById(requestId)
                .orElseThrow(() -> new BusinessException(RequiredErrorCode.CHATTING_NOT_FOUND));
    }

    public Optional<ChattingRequired> findLatest(Member requester, Member requestee) {
        return chattingRequiredRepository.findLatest(requester, requestee);
    }

    public ChattingRequired savePending(Member requester, Member requestee, @Nullable RoommateBoard roommateBoard) {
        return save(requester, requestee, roommateBoard, ChattingRequiredStatus.PENDING);
    }

    public ChattingRequired saveAccepted(Member requester, Member requestee, @Nullable RoommateBoard roommateBoard) {
        return save(requester, requestee, roommateBoard, ChattingRequiredStatus.ACCEPTED);
    }

    private ChattingRequired save(Member requester, Member requestee, @Nullable RoommateBoard roommateBoard, ChattingRequiredStatus status) {
        ChattingRequired chattingRequired = ChattingRequired.builder()
                .requester(requester)
                .requestee(requestee)
                .roommateBoard(roommateBoard)
                .status(status)
                .build();

        return chattingRequiredRepository.save(chattingRequired);
    }
}
