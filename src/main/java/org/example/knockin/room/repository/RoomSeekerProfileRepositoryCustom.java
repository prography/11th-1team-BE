package org.example.knockin.room.repository;

import java.util.List;
import org.example.knockin.room.repository.row.MatchingSeekerProfileRow;
import org.example.knockin.room.repository.row.MatchingSeekerRegionRow;
import org.example.knockin.room.repository.row.MatchingSeekerRoomTypeRow;

public interface RoomSeekerProfileRepositoryCustom {
    List<MatchingSeekerProfileRow> findAllSeekerProfileByMemberIdIn(List<Long> memberIds);

    List<MatchingSeekerRegionRow> findAllSeekerRegionByMemberIdIn(List<Long> memberIds);

    List<MatchingSeekerRoomTypeRow> findAllSeekerRoomTypeByMemberIdIn(List<Long> memberIds);
}
