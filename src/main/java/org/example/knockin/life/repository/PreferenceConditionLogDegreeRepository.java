package org.example.knockin.life.repository;

import org.example.knockin.life.entity.PreferenceConditionLogDegree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceConditionLogDegreeRepository extends JpaRepository<PreferenceConditionLogDegree, Long>, PreferenceConditionLogDegreeRepositoryCustom {
}