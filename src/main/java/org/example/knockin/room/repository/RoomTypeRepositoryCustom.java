package org.example.knockin.room.repository;

import org.example.knockin.room.dto.MetaRoomTypesDto;
import org.example.knockin.room.entity.RoomType;

import java.util.List;

public interface RoomTypeRepositoryCustom {
    List<RoomType> findByRoomTypes(List<Long> roomTypes);
    List<MetaRoomTypesDto.Response.RoomTypeItem> findAllByIsDeleted(Boolean isDeleted);
}