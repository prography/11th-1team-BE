package org.example.knockin.life.repository;

import org.example.knockin.life.entity.LifePatternInformation;
import org.example.knockin.life.repository.row.LifePatternInformationValueRow;

import java.util.List;

public interface LifePatternInformationRepositoryCustom {
    List<LifePatternInformation> findByLifeStyles(List<Long> lifeStyles);

    List<LifePatternInformationValueRow> findAllValueRowsByLifePatternIdIn(List<Long> lifePatternIds);
}