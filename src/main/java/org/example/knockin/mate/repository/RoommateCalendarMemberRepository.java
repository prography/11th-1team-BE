package org.example.knockin.mate.repository;

import java.util.List;
import org.example.knockin.mate.entity.RoommateCalendar;
import org.example.knockin.mate.entity.RoommateCalendarMember;
import org.example.knockin.mate.entity.RoommateCalendarMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateCalendarMemberRepository extends JpaRepository<RoommateCalendarMember, RoommateCalendarMemberId>, RoommateCalendarMemberRepositoryCustom {
    List<RoommateCalendarMember> findByRoommateCalendar(RoommateCalendar roommateCalendar);
}
