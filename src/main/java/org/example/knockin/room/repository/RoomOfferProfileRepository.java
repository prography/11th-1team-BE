package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomOfferProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomOfferProfileRepository extends JpaRepository<RoomOfferProfile, Long>, RoomOfferProfileRepositoryCustom {
}