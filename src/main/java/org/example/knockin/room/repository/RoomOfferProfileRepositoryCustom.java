package org.example.knockin.room.repository;

import java.util.List;
import org.example.knockin.room.repository.row.MatchingOfferProfileRow;

public interface RoomOfferProfileRepositoryCustom {
    List<MatchingOfferProfileRow> findAllOfferProfileByMemberIdIn(List<Long> memberIds);
}
