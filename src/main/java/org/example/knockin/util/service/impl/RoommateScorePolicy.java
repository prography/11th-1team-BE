package org.example.knockin.util.service.impl;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "policy.roommate-score")
public class RoommateScorePolicy {
    @Min(1)
    private int perfectScore;

    @Min(1)
    private int importantPatternMultiplier;

    int importanceMultiplier(boolean important) {
        return important ? importantPatternMultiplier : 1;
    }

    int toScore(double rawScore, int maxRawScore) {
        if (maxRawScore <= 0) {
            return 0;
        }
        return clampScore((int) Math.round(rawScore / maxRawScore * perfectScore));
    }

    int toPercent(double similarity) {
        return clampScore((int) Math.round(similarity * perfectScore));
    }

    double clampSimilarity(double similarity) {
        return Math.max(0.0, Math.min(1.0, similarity));
    }

    private int clampScore(int score) {
        return Math.max(0, Math.min(perfectScore, score));
    }
}
