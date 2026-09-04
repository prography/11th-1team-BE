package org.example.knockin.life.repository;

import java.util.List;
import org.example.knockin.life.entity.PreferenceConditionWeightLog;

public interface PreferenceConditionWeightLogRepositoryCustom {
    List<PreferenceConditionWeightLog> findLatestLogsWithFetchByMemberId(Long memberId);
}
