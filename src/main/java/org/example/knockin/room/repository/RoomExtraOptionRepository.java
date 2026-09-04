package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomExtraOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomExtraOptionRepository extends JpaRepository<RoomExtraOption, Long>, RoomExtraOptionRepositoryCustom {
    Page<RoomExtraOption> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
}