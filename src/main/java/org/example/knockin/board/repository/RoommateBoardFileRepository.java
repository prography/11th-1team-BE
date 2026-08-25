package org.example.knockin.board.repository;

import java.util.List;
import org.example.knockin.board.entity.RoommateBoard;
import org.example.knockin.board.entity.RoommateBoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateBoardFileRepository extends JpaRepository<RoommateBoardFile, Long>, RoommateBoardFileRepositoryCustom {
    List<RoommateBoardFile> findByRoommateBoard(RoommateBoard roommateBoard);
}