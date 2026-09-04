package org.example.knockin.mate.repository.impl;

import static org.example.knockin.mate.entity.QExcludeRoommateCalendar.excludeRoommateCalendar;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.repository.ExcludeRoommateCalendarRepositoryCustom;
import org.example.knockin.mate.repository.row.RepeatCalendarExcludeRow;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExcludeRoommateCalendarRepositoryImpl implements ExcludeRoommateCalendarRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RepeatCalendarExcludeRow> findRepeatCalendarExcludes(List<Long> repeatCalendarIds) {
        if (repeatCalendarIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .select(Projections.constructor(
                        RepeatCalendarExcludeRow.class,
                        excludeRoommateCalendar.repeatRoommateCalendar.id,
                        excludeRoommateCalendar.excludeAt
                ))
                .from(excludeRoommateCalendar)
                .where(excludeRoommateCalendar.repeatRoommateCalendar.id.in(repeatCalendarIds))
                .fetch();
    }

}
