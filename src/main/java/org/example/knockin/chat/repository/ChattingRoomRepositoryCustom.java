package org.example.knockin.chat.repository;

import java.util.List;
import org.example.knockin.chat.repository.row.ChatRoomListRow;

public interface ChattingRoomRepositoryCustom {
    List<ChatRoomListRow> findListRowsByMemberId(Long memberId);

    boolean existsActiveRoomBetweenMembers(Long memberAId, Long memberBId);

    long countActiveRoomsByMemberId(Long memberId);
}
