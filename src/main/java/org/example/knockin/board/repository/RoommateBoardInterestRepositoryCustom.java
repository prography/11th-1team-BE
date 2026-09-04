package org.example.knockin.board.repository;

import java.util.Collection;
import java.util.List;
import org.example.knockin.board.repository.row.BoardInterestCountRow;

public interface RoommateBoardInterestRepositoryCustom {

    List<Long> findActiveBoardIdsByMemberIdAndBoardIds(Long memberId, Collection<Long> boardIds);

    List<BoardInterestCountRow> findActiveInterestCountsByBoardIds(List<Long> boardIds);
}
