package org.example.knockin.mate.repository.impl;

import static org.example.knockin.member.entity.QBasicInformation.basicInformation;
import static org.example.knockin.mate.entity.QRoommateCalendarMember.roommateCalendarMember;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.repository.RoommateCalendarMemberRepositoryCustom;
import org.example.knockin.mate.repository.row.DailyCalendarMemberRow;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoommateCalendarMemberRepositoryImpl implements RoommateCalendarMemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DailyCalendarMemberRow> findDailyCalendarMembers(List<Long> calendarIds) {
        if (calendarIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .select(Projections.constructor(
                        DailyCalendarMemberRow.class,
                        roommateCalendarMember.roommateCalendar.id,
                        roommateCalendarMember.member.id,
                        basicInformation.name
                ))
                .from(roommateCalendarMember)
                .leftJoin(basicInformation)
                .on(basicInformation.id.eq(
                        JPAExpressions
                                .select(basicInformation.id.max())
                                .from(basicInformation)
                                .where(basicInformation.member.id.eq(roommateCalendarMember.member.id))
                ))
                .where(roommateCalendarMember.roommateCalendar.id.in(calendarIds))
                .orderBy(roommateCalendarMember.roommateCalendar.id.asc(), roommateCalendarMember.member.id.asc())
                .fetch();
    }
}
