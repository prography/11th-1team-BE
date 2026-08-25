package org.example.knockin.life.repository.row;

import org.example.knockin.life.entity.LifePatternType;
import org.example.knockin.global.util.HasMemberId;

public record MatchingLifestyleRow(
        Long memberId,
        Long lifestyleId,
        Long lifePatternId,
        Long lifePatternInformationId,
        String name,
        String value,
        String description,
        LifePatternType type,
        String imageUrl
) implements HasMemberId {
}
