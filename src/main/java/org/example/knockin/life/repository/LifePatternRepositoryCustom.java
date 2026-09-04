package org.example.knockin.life.repository;

import org.example.knockin.life.dto.BoLifeStylePatternDetailDto;
import org.example.knockin.life.dto.BoLifeStylePatternListDto;
import org.example.knockin.life.dto.MetaLifestylePatternsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LifePatternRepositoryCustom {
    List<MetaLifestylePatternsDto.Response.PatternItem> findLifeStylePatterns();
    List<BoLifeStylePatternListDto.Response.PatternItem> findLifeStylePatternList(Pageable pageable);
    BoLifeStylePatternDetailDto.Response findLifeStylePattern(Long patternId);
}