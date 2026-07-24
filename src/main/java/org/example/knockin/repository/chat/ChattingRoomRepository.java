package org.example.knockin.repository.chat;

import java.util.Optional;
import org.example.knockin.entity.chat.ChattingRequired;
import org.example.knockin.entity.chat.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long>, ChattingRoomRepositoryCustom {
    Optional<ChattingRoom> findByChattingRequired(ChattingRequired chattingRequired);
}
