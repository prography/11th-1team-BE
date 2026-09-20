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
public class SingleChoiceScoreCalc implements LifePatternTypeScoreCalc {
    @Override
    public LifePatternType supports() {
        return LifePatternType.SINGLE_CHOICE;
    }

    @Override
    public Integer calculateSimilarity(LifePatternInformation source, LifePatternInformation target, LifePattern lifePattern) {
        return Objects.equals(source.getDvalue(), target.getDvalue()) ? 100 : 0;
    }
}
