package org.example.knockin.mate.repository;

import org.example.knockin.mate.entity.ExcludeRoommateCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcludeRoommateCalendarRepository extends JpaRepository<ExcludeRoommateCalendar, Long>, ExcludeRoommateCalendarRepositoryCustom {
}
