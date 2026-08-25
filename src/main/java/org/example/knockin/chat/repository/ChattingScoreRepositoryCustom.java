package org.example.knockin.chat.repository;

import java.util.Optional;
import org.example.knockin.chat.entity.ChattingScore;

public interface ChattingScoreRepositoryCustom {
    Optional<ChattingScore> findOneByChattingRequiredIdAndMemberId(Long chattingRequiredId, Long memberId);
}