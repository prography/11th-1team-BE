package org.example.knockin.chat.repository;

import org.example.knockin.chat.entity.ChatRoomFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomFileRepository extends JpaRepository<ChatRoomFile, Long> {
}
