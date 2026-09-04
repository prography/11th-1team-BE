package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomSeekerProfile;
import org.example.knockin.room.entity.RoomSeekerProfileRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomSeekerProfileRegionRepository extends JpaRepository<RoomSeekerProfileRegion, Long> {
    void deleteByRoomSeekerProfile(RoomSeekerProfile roomSeekerProfile);
}