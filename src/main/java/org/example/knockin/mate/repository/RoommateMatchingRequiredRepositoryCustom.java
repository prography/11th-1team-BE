package org.example.knockin.mate.repository;

import java.util.List;
import java.util.Optional;
import org.example.knockin.mate.dto.RoommateRequestDto.RoommateMatchingRequiredInfo;
import org.example.knockin.chat.entity.ChattingRoom;
import org.example.knockin.mate.entity.RoommateMatchingRequired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoommateMatchingRequiredRepositoryCustom {
    Optional<RoommateMatchingRequired> findLatest(Long chattingRoomId);

    List<RoommateMatchingRequiredInfo> findRequiredDto(ChattingRoom chattingRoomEntity);

    Page<RoommateMatchingRequired> findMyRequiredList(Long memberId, Pageable pageable);
}
