package org.example.knockin.board.repository;

import java.util.List;
import org.example.knockin.board.dto.BoardDetailDto.Response.RoomExtraOptionInfo;
import org.example.knockin.board.entity.RoommateBoardOption;

public interface RoommateBoardOptionRepositoryCustom {
    List<RoomExtraOptionInfo> getExtraOptionsByBoardId(Long boardId);

    List<RoommateBoardOption> findWithRoomExtraOptionByBoardId(Long boardId);
}
