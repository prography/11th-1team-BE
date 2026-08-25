package org.example.knockin.life.repository;

import org.example.knockin.life.entity.PreferenceConditionWeightLogDegree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceConditionWeightLogDegreeRepository extends JpaRepository<PreferenceConditionWeightLogDegree, Long>, PreferenceConditionWeightLogDegreeRepositoryCustom {
}