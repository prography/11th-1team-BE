package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomType;
import org.example.knockin.room.entity.RoomTypeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeFileRepository extends JpaRepository<RoomTypeFile, Long> {
    Optional<RoomTypeFile> findByRoomType(RoomType roomType);
}