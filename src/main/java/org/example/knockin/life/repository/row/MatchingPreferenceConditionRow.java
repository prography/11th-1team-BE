package org.example.knockin.life.repository.row;

import org.example.knockin.life.entity.LifePatternType;
import org.example.knockin.global.util.HasMemberId;

public record MatchingPreferenceConditionRow(
        Long memberId,
        Long conditionId,
        Long lifePatternId,
        Long lifePatternInformationId,
        String name,
        String value,
        String description,
        LifePatternType type,
        String imageUrl
) implements HasMemberId {
}