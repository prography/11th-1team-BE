package org.example.knockin.life.repository;

import org.example.knockin.life.entity.PreferenceConditionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceConditionLogRepository extends JpaRepository<PreferenceConditionLog, Long>, PreferenceConditionLogRepositoryCustom {
}