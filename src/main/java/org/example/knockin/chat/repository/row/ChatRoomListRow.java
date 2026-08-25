package org.example.knockin.chat.repository.row;

import java.time.LocalDateTime;
import org.example.knockin.mate.entity.RoommateRequiredStatus;

public record ChatRoomListRow(
        Long chatRoomId,
        Long opponentMemberId,
        String memberName,
        String memberProfileImageUrl,
        LocalDateTime createdAt,
        RoommateRequiredStatus roommateStatus,
        Boolean isRoommate,
        String lastMessage,
        LocalDateTime lastMessageAt
) {
}
