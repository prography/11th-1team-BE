package org.example.knockin.chat.repository;

import org.example.knockin.chat.entity.ChatRoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long>, ChatRoomMessageRepositoryCustom {
}
