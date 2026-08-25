package org.example.knockin.board.repository;

import java.util.List;
import org.example.knockin.board.dto.BoardDetailDto.Response.FileDetailDto;
import org.example.knockin.board.repository.row.BoardThumbnailRow;

public interface RoommateBoardFileRepositoryCustom {
    List<FileDetailDto> getFileDetailDtoByBoardId(Long boardId);

    List<BoardThumbnailRow> findThumbnailsByBoardIds(List<Long> boardIds);
}
