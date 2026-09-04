package org.example.knockin.chat.repository.row;

public record ChatRoomUnreadCountRow(
        Long chatRoomId,
        Long messageCount
) {
}
