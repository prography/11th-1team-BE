package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomSeekerProfileRepository extends JpaRepository<RoomSeekerProfile, Long>, RoomSeekerProfileRepositoryCustom {
}