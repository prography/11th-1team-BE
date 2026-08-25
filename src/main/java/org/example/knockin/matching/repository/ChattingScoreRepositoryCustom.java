package org.example.knockin.matching.repository;

import java.util.Optional;
import org.example.knockin.matching.entity.ChattingScore;

public interface ChattingScoreRepositoryCustom {
    Optional<ChattingScore> findOneByChattingRequiredIdAndMemberId(Long chattingRequiredId, Long memberId);
}