package org.example.knockin.room.repository;

import org.example.knockin.room.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long>, RoomTypeRepositoryCustom {
    Page<RoomType> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
}