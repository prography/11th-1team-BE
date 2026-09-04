package org.example.knockin.life.repository;

import org.example.knockin.member.entity.Member;

import java.util.Optional;

public interface PreferenceConditionLogDegreeRepositoryCustom {
    Optional<Long> findMaxPreferenceConditionLogDegree(Member member);
}
