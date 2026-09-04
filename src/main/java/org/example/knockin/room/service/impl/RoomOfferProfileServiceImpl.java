package org.example.knockin.room.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.room.repository.RoomOfferProfileRepository;
import org.example.knockin.room.repository.row.MatchingOfferProfileRow;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomOfferProfileServiceImpl {

    private final RoomOfferProfileRepository roomOfferProfileRepository;

    public List<MatchingOfferProfileRow> findAllOfferProfileByMemberIdIn(List<Long> memberIds) {
        return roomOfferProfileRepository.findAllOfferProfileByMemberIdIn(memberIds);
    }
}
