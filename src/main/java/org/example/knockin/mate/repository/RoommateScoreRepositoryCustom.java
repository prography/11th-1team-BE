package org.example.knockin.mate.repository;

import java.util.Optional;
import org.example.knockin.mate.entity.RoommateScore;

public interface RoommateScoreRepositoryCustom {
    Optional<RoommateScore> findOneByMyRoommateIdAndMemberId(Long myRoommateId, Long memberId);
}
