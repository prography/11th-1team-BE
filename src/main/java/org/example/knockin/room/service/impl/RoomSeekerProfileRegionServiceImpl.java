package org.example.knockin.room.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.room.entity.RoomSeekerProfile;
import org.example.knockin.room.entity.RoomSeekerProfileRegion;
import org.example.knockin.room.repository.RoomSeekerProfileRegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomSeekerProfileRegionServiceImpl {
    private final RoomSeekerProfileRegionRepository roomSeekerProfileRegionRepository;

    @Transactional
    public List<RoomSeekerProfileRegion> saveAll(List<RoomSeekerProfileRegion> roomSeekerProfileRegionList) {
        return roomSeekerProfileRegionRepository.saveAll(roomSeekerProfileRegionList);
    }

    @Transactional
    public RoomSeekerProfile deleteByRoomSeekerProfile(RoomSeekerProfile seekerProfile) {
        roomSeekerProfileRegionRepository.deleteByRoomSeekerProfile(seekerProfile);
        return seekerProfile;
    }
}
