package org.example.knockin.life.repository;

import java.util.List;
import org.example.knockin.life.entity.PreferenceConditionLog;

public interface PreferenceConditionLogRepositoryCustom {
    List<PreferenceConditionLog> findLatestLogsWithFetchByMemberId(Long memberId, List<Long> lifePatternInformationIds);
}
