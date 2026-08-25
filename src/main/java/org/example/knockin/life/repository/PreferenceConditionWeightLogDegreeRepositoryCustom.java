package org.example.knockin.life.repository;

import org.example.knockin.member.entity.Member;

import java.util.Optional;

public interface PreferenceConditionWeightLogDegreeRepositoryCustom {
    Optional<Long> findMaxPreferenceConditionWeightLogDegree(Member member);
}
