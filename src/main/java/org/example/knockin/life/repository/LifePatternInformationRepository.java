package org.example.knockin.life.repository;

import org.example.knockin.life.entity.LifePattern;
import org.example.knockin.life.entity.LifePatternInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LifePatternInformationRepository extends JpaRepository<LifePatternInformation, Long>, LifePatternInformationRepositoryCustom {
    List<LifePatternInformation> findByLifePattern(LifePattern lifePattern);

    void deleteByLifePattern(LifePattern lifePattern);
}