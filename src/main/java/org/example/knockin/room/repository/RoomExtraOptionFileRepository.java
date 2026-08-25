package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomExtraOption;
import org.example.knockin.room.entity.RoomExtraOptionFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomExtraOptionFileRepository extends JpaRepository<RoomExtraOptionFile, Long> {
    Optional<RoomExtraOptionFile> findByRoomExtraOption(RoomExtraOption roomExtraOption);
}