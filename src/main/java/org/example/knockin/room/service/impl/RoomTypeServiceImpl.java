package org.example.knockin.room.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.knockin.room.dto.MetaRoomTypesDto;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.RoomTypeErrorCode;
import org.example.knockin.room.repository.OfferRoomTypeRepository;
import org.example.knockin.room.repository.RoomTypeFileRepository;
import org.example.knockin.room.repository.RoomTypeRepository;
import org.example.knockin.room.repository.SeekerRoomTypeRepository;
import org.example.knockin.room.entity.OfferRoomType;
import org.example.knockin.room.entity.RoomOfferProfile;
import org.example.knockin.room.entity.RoomSeekerProfile;
import org.example.knockin.room.entity.RoomType;
import org.example.knockin.room.entity.RoomTypeFile;
import org.example.knockin.room.entity.SeekerRoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeFileRepository roomTypeFileRepository;
    private final OfferRoomTypeRepository offerRoomTypeRepository;
    private final SeekerRoomTypeRepository seekerRoomTypeRepository;

    @Transactional
    public RoomType saveRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public RoomTypeFile saveRoomTypeFile(RoomTypeFile roomTypeFile) {
        return roomTypeFileRepository.save(roomTypeFile);
    }

    @Transactional
    public RoomType modifyRoomType(RoomType roomType, Long roomTypeId) {
        RoomType roomTypeEntity = roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new BusinessException(RoomTypeErrorCode.ROOM_TYPE_NOT_FOUND));
        roomTypeEntity.modifyRoomType(roomType);
        return roomTypeEntity;
    }

    @Transactional
    public RoomType deleteRoomType(Long roomTypeId) {
        RoomType roomTypeEntity = roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new BusinessException(RoomTypeErrorCode.ROOM_TYPE_NOT_FOUND));
        roomTypeEntity.deleteRoomType();
        return roomTypeEntity;
    }

    public List<RoomType> findRoomTypeList(Pageable pageable) {
        return roomTypeRepository.findAllByIsDeleted(false, pageable).stream().toList();
    }

    public RoomType findRoomType(Long roomTypeId) {
        return roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new BusinessException(RoomTypeErrorCode.ROOM_TYPE_NOT_FOUND));
    }

    public List<RoomType> findByRoomTypes(List<Long> roomTypes) {
        return roomTypeRepository.findByRoomTypes(roomTypes);
    }

    public List<MetaRoomTypesDto.Response.RoomTypeItem> findAllByIsDeleted(boolean isDeleted) {
        return roomTypeRepository.findAllByIsDeleted(isDeleted);
    }

    @Transactional
    public List<OfferRoomType> saveOfferRoomTypeAll(List<OfferRoomType> offerRoomTypeList) {
        return offerRoomTypeRepository.saveAll(offerRoomTypeList);
    }

    @Transactional
    public List<SeekerRoomType> saveSeekerRoomTypeAll(List<SeekerRoomType> seekerRoomTypes) {
        return seekerRoomTypeRepository.saveAll(seekerRoomTypes);
    }

    @Transactional
    public RoomOfferProfile deleteByRoomOfferProfile(RoomOfferProfile roomOfferProfile) {
        offerRoomTypeRepository.deleteByRoomOfferProfile(roomOfferProfile);
        return roomOfferProfile;
    }

    @Transactional
    public RoomSeekerProfile deleteByRoomSeekerProfile(RoomSeekerProfile seekerProfile) {
        seekerRoomTypeRepository.deleteByRoomSeekerProfile(seekerProfile);
        return seekerProfile;
    }

    public RoomTypeFile findRoomTypeFile(RoomType roomType) {
        return roomTypeFileRepository.findByRoomType(roomType).orElse(null);
    }
}
