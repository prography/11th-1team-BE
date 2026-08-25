package org.example.knockin.mate.repository;

import java.util.List;
import org.example.knockin.mate.entity.MyRoommate;
import org.example.knockin.mate.entity.RoommateCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateCalendarRepository extends JpaRepository<RoommateCalendar, Long>, RoommateCalendarRepositoryCustom {
    List<RoommateCalendar> findByMyRoommate(MyRoommate myRoommate);
}