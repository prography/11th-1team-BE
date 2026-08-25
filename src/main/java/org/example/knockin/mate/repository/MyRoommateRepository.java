package org.example.knockin.mate.repository;

import org.example.knockin.mate.entity.MyRoommate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRoommateRepository extends JpaRepository<MyRoommate, Long>, MyRoommateRepositoryCustom {
}