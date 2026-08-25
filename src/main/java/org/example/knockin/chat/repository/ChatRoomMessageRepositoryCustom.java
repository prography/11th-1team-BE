package org.example.knockin.chat.repository;

import java.util.List;
import org.example.knockin.chat.dto.ChatRoomDetailDto.ChatMessage;
import org.example.knockin.chat.repository.row.ChatRoomUnreadCountRow;

public interface ChatRoomMessageRepositoryCustom {
    List<ChatMessage> findChatMessageDto(Long chatRoomId);

    List<ChatRoomUnreadCountRow> findUnreadMessageCounts(Long memberId, List<Long> chatRoomIds);

    long markUnreadMessagesAsRead(Long chatRoomId, Long memberId);
}
