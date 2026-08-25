package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomSeekerProfile;
import org.example.knockin.room.entity.SeekerRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeekerRoomTypeRepository extends JpaRepository<SeekerRoomType, Long> {
    void deleteByRoomSeekerProfile(RoomSeekerProfile roomSeekerProfile);
}