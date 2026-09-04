package org.example.knockin.mate.repository;

import java.util.Optional;
import org.example.knockin.mate.entity.RepeatRoommateCalendar;
import org.example.knockin.mate.entity.RoommateCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatRoommateCalendarRepository extends JpaRepository<RepeatRoommateCalendar, Long> {
    Optional<RepeatRoommateCalendar> findOneByRoommateCalendar(RoommateCalendar roommateCalendar);
}
