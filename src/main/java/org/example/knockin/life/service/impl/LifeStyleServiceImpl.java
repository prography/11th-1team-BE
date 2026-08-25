package org.example.knockin.life.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.life.dto.BoLifeStylePatternDetailDto;
import org.example.knockin.life.dto.BoLifeStylePatternListDto;
import org.example.knockin.life.dto.MetaLifestylePatternsDto;
import org.example.knockin.life.entity.LifePattern;
import org.example.knockin.life.entity.LifePatternFile;
import org.example.knockin.life.entity.LifePatternInformation;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.LifePatternErrorCode;
import org.example.knockin.life.repository.LifePatternFileRepository;
import org.example.knockin.life.repository.LifePatternInformationRepository;
import org.example.knockin.life.repository.LifePatternRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LifeStyleServiceImpl {
    private final LifePatternRepository lifePatternRepository;
    private final LifePatternFileRepository lifePatternFileRepository;
    private final LifePatternInformationRepository lifePatternInformationRepository;

    @Transactional
    public LifePattern saveLifePattern(LifePattern lifePattern) {
        return lifePatternRepository.save(lifePattern);
    }

    @Transactional
    public LifePatternFile saveLifePatternFile(LifePatternFile lifePatternFile) {
        return lifePatternFileRepository.save(lifePatternFile);
    }

    @Transactional
    public List<LifePatternInformation> saveLifePatternInformation(List<LifePatternInformation> lifePatternInformation) {
        return lifePatternInformationRepository.saveAll(lifePatternInformation);
    }

    @Transactional
    public LifePatternInformation saveLifeInformation(LifePatternInformation lifePatternInformation) {
        return lifePatternInformationRepository.save(lifePatternInformation);
    }

    @Transactional
    public LifePattern deleteLifePattern(Long patternId) {
        LifePattern lifePattern = lifePatternRepository.findById(patternId).orElseThrow(() -> new BusinessException(LifePatternErrorCode.LIFE_PATTERN_NOT_FOUNT));
        lifePattern.deleteLifePattern();
        return lifePattern;
    }

    @Transactional
    public void deleteLifeInformationByPattern(LifePattern lifePattern) {
        lifePatternInformationRepository.deleteByLifePattern(lifePattern);
    }

    public List<LifePatternInformation> findLifeInformationByPattern(LifePattern lifePattern) {
        return lifePatternInformationRepository.findByLifePattern(lifePattern);
    }

    @Transactional
    public void deleteLifeInformation(LifePatternInformation lifePatternInformation) {
        lifePatternInformationRepository.delete(lifePatternInformation);
    }

    public BoLifeStylePatternListDto.Response findLifeStylePatternList(Pageable pageable) {
        List<BoLifeStylePatternListDto.Response.PatternItem> patternItemList = lifePatternRepository.findLifeStylePatternList(pageable);
        return BoLifeStylePatternListDto.Response.builder().patterns(patternItemList).build();
    }

    public BoLifeStylePatternDetailDto.Response findLifeStylePattern(Long patternId) {
        return lifePatternRepository.findLifeStylePattern(patternId);
    }

    public LifePattern findLifeStyle(Long patternId) {
        return lifePatternRepository.findById(patternId).orElseThrow(() -> new BusinessException(LifePatternErrorCode.LIFE_PATTERN_NOT_FOUNT));
    }

    public LifePatternFile findLifeStyleFile(LifePattern lifePattern) {
        return lifePatternFileRepository.findByLifePattern(lifePattern).orElse(null);
    }

    public List<LifePattern> findAllById(List<Long> lifeStyles) {
        if (lifeStyles == null || lifeStyles.isEmpty()) {
            return Collections.emptyList();
        }
        return lifePatternRepository.findAllById(lifeStyles);
    }

    public List<LifePatternInformation> findByLifeStyles(List<Long> lifeStyles) {
        if (lifeStyles == null || lifeStyles.isEmpty()) {
            return Collections.emptyList();
        }
        return lifePatternInformationRepository.findByLifeStyles(lifeStyles);
    }

    public List<MetaLifestylePatternsDto.Response.PatternItem> findLifeStylePatterns() {
        return lifePatternRepository.findLifeStylePatterns();
    }

    public List<LifePatternInformation> findLifePatternInformationAllById(List<Long> lifestyleIds) {
        if (lifestyleIds == null || lifestyleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return lifePatternInformationRepository.findAllById(lifestyleIds);
    }

    public LifePatternInformation findLifePatternInformationById(Long lifestyleId) {
        return lifePatternInformationRepository.findById(lifestyleId).orElse(null);
    }
}
