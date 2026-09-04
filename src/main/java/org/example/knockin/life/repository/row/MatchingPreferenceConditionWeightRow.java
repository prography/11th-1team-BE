package org.example.knockin.life.repository.row;

import org.example.knockin.global.util.HasMemberId;

public record MatchingPreferenceConditionWeightRow(
        Long memberId,
        Long conditionWeightId,
        Long lifePatternId,
        String name,
        String imageUrl
) implements HasMemberId {
}
