package org.example.knockin.board.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.example.knockin.board.entity.RoommateBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoommateBoardRepository extends JpaRepository<RoommateBoard, Long>, RoommateBoardRepositoryCustom {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE RoommateBoard r SET r.hits = r.hits + 1 WHERE r.id = :boardId AND r.isDeleted = false")
    int increaseHitsById(@Param("boardId") Long boardId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RoommateBoard r WHERE r.id = :boardId AND r.isDeleted = false")
    Optional<RoommateBoard> findByIdForUpdate(@Param("boardId") Long boardId);
}