package org.example.knockin.room.repository;

import org.example.knockin.room.dto.MetaRoomAddOptionsDto;

import java.util.List;

public interface RoomExtraOptionRepositoryCustom {
    List<MetaRoomAddOptionsDto.Response.RoomAddOptionItem> findAllByIsDeleted(Boolean isDeleted);
}
