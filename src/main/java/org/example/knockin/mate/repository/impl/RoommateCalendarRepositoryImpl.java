package org.example.knockin.mate.repository.impl;

import static org.example.knockin.mate.entity.QRepeatRoommateCalendar.repeatRoommateCalendar;
import static org.example.knockin.mate.entity.QRoommateCalendar.roommateCalendar;
import static org.example.knockin.mate.entity.QRoommateCalendarCategory.roommateCalendarCategory;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.entity.QRepeatRoommateCalendar;
import org.example.knockin.mate.repository.RoommateCalendarRepositoryCustom;
import org.example.knockin.mate.repository.row.DailyCalendarRow;
import org.example.knockin.mate.repository.row.MonthlyCalendarRow;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoommateCalendarRepositoryImpl implements RoommateCalendarRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DailyCalendarRow> findDailyCalendarList(Long myRoommateId, LocalDateTime from, LocalDateTime to) {
        QRepeatRoommateCalendar latestRepeat = new QRepeatRoommateCalendar("latestRepeat");

        return jpaQueryFactory
                .select(Projections.constructor(
                        DailyCalendarRow.class,
                        roommateCalendar.id,
                        roommateCalendar.title,
                        roommateCalendar.contents,
                        roommateCalendar.startDate,
                        roommateCalendar.endDate,
                        roommateCalendarCategory.name,
                        repeatRoommateCalendar.id,
                        repeatRoommateCalendar.endDate,
                        repeatRoommateCalendar.repeatType
                ))
                .from(roommateCalendar)
                .join(roommateCalendar.roommateCalendarCategory, roommateCalendarCategory)
                .leftJoin(repeatRoommateCalendar)
                .on(repeatRoommateCalendar.id.eq(
                        JPAExpressions
                                .select(latestRepeat.id.max())
                                .from(latestRepeat)
                                .where(latestRepeat.roommateCalendar.eq(roommateCalendar))
                ))
                .where(
                        roommateCalendar.myRoommate.id.eq(myRoommateId),
                        calendarRange(from, to),
                        roommateCalendar.isDeleted.isFalse()
                )
                .orderBy(roommateCalendar.startDate.asc(), roommateCalendar.id.asc())
                .fetch();
    }

    @Override
    public List<MonthlyCalendarRow> findMonthlyCalendarList(Long myRoommateId, LocalDateTime from, LocalDateTime to) {
        QRepeatRoommateCalendar latestRepeat = new QRepeatRoommateCalendar("latestRepeat");

        return jpaQueryFactory
                .select(Projections.constructor(
                        MonthlyCalendarRow.class,
                        roommateCalendar.id,
                        roommateCalendar.startDate,
                        roommateCalendar.endDate,
                        repeatRoommateCalendar.id,
                        repeatRoommateCalendar.endDate,
                        repeatRoommateCalendar.repeatType
                ))
                .from(roommateCalendar)
                .leftJoin(repeatRoommateCalendar)
                .on(repeatRoommateCalendar.id.eq(
                        JPAExpressions
                                .select(latestRepeat.id.max())
                                .from(latestRepeat)
                                .where(latestRepeat.roommateCalendar.eq(roommateCalendar))
                ))
                .where(
                        roommateCalendar.myRoommate.id.eq(myRoommateId),
                        calendarRange(from, to),
                        roommateCalendar.isDeleted.isFalse()
                )
                .orderBy(roommateCalendar.startDate.asc(), roommateCalendar.id.asc())
                .fetch();
    }

    private BooleanExpression calendarRange(LocalDateTime from, LocalDateTime to) {
        BooleanExpression basicCalendarRange = repeatRoommateCalendar.id.isNull()
                .and(roommateCalendar.startDate.lt(to))
                .and(roommateCalendar.endDate.gt(from));

        BooleanExpression repeatCalendarRange = repeatRoommateCalendar.id.isNotNull()
                .and(roommateCalendar.startDate.lt(to))
                .and(repeatRoommateCalendar.endDate.gt(from));

        return basicCalendarRange.or(repeatCalendarRange);
    }

}
