package org.example.knockin.board.repository;

import org.example.knockin.board.entity.RoommateBoardOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateBoardOptionRepository extends JpaRepository<RoommateBoardOption, Long>, RoommateBoardOptionRepositoryCustom {
}