package org.example.knockin.room.repository;

import org.example.knockin.room.entity.OfferRoomType;
import org.example.knockin.room.entity.RoomOfferProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRoomTypeRepository extends JpaRepository<OfferRoomType, Long> {
    void deleteByRoomOfferProfile(RoomOfferProfile roomOfferProfile);
}