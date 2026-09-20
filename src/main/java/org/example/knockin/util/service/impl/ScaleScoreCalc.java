package org.example.knockin.util.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.life.entity.LifePattern;
import org.example.knockin.life.entity.LifePatternInformation;
import org.example.knockin.life.entity.LifePatternType;
import org.example.knockin.life.repository.LifePatternInformationRepository;
import org.example.knockin.util.service.LifePatternTypeScoreCalc;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ScaleScoreCalc implements LifePatternTypeScoreCalc {
    private final LifePatternInformationRepository lifePatternInformationRepository;
    private static final int TOTAL_POINT = 100;

    @Override
    public Integer calculateSimilarity(LifePatternInformation source, LifePatternInformation target, LifePattern lifePattern) {
        List<LifePatternInformation> lifePatternInformations = lifePatternInformationRepository.findByLifePattern(lifePattern);
        int min = lifePatternInformations.stream().mapToInt(item -> Integer.parseInt(item.getDvalue())).min().orElse(0);
        int max = lifePatternInformations.stream().mapToInt(item -> Integer.parseInt(item.getDvalue())).max().orElse(0);

        double lifePatternPartPoint = (double) TOTAL_POINT / (max - min);
        int diff = Math.abs(Integer.parseInt(source.getDvalue()) - Integer.parseInt(target.getDvalue()));

        return (int) (TOTAL_POINT - lifePatternPartPoint * diff);
    }

    @Override
    public LifePatternType supports() {
        return LifePatternType.SCALE;
    }
}
