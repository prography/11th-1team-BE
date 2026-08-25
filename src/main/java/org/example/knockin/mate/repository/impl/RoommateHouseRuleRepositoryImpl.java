package org.example.knockin.mate.repository.impl;

import static org.example.knockin.mate.entity.QRoommateHouseRule.roommateHouseRule;
import static org.example.knockin.mate.entity.QMyRoommate.myRoommate;
import static org.example.knockin.mate.entity.QRoommateMatchingRequired.roommateMatchingRequired;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.entity.RoommateHouseRule;
import org.example.knockin.mate.repository.RoommateHouseRuleRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoommateHouseRuleRepositoryImpl implements RoommateHouseRuleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<RoommateHouseRule> findWithFetchedById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(roommateHouseRule)
                        .from(roommateHouseRule)
                        .join(roommateHouseRule.myRoommate, myRoommate).fetchJoin()
                        .join(myRoommate.roommateMatchingRequired, roommateMatchingRequired)
                        .where(
                                roommateHouseRule.id.eq(id),
                                roommateHouseRule.isDeleted.isFalse()
                        )
                        .fetchOne()
        );
    }
}
