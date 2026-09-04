package org.example.knockin.life.repository;

import org.example.knockin.life.entity.LifePattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifePatternRepository extends JpaRepository<LifePattern, Long>, LifePatternRepositoryCustom {
}