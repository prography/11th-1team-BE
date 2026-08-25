package org.example.knockin.matching.repository;

import java.util.Optional;
import org.example.knockin.matching.entity.RoommateScore;

public interface RoommateScoreRepositoryCustom {
    Optional<RoommateScore> findOneByMyRoommateIdAndMemberId(Long myRoommateId, Long memberId);
}
