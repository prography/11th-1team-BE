package org.example.knockin.life.repository;

import org.example.knockin.life.entity.PreferenceCondition;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceConditionRepository extends JpaRepository<PreferenceCondition, Long>, PreferenceConditionRepositoryCustom {
    List<PreferenceCondition> findByMember(Member member);
}