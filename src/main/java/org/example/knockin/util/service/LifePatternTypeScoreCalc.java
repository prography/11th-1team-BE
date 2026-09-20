package org.example.knockin.util.service;

import org.example.knockin.life.entity.LifePattern;
import org.example.knockin.life.entity.LifePatternInformation;
import org.example.knockin.life.entity.LifePatternType;

public interface LifePatternTypeScoreCalc {
    Integer calculateSimilarity(LifePatternInformation source, LifePatternInformation target, LifePattern lifePattern);
    LifePatternType supports();
}
