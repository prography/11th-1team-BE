package org.example.knockin.life.repository;

import org.example.knockin.life.entity.PreferenceConditionWeight;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceConditionWeightRepository extends JpaRepository<PreferenceConditionWeight, Long>, PreferenceConditionWeightRepositoryCustom {
    void deleteByMember(Member member);

    List<PreferenceConditionWeight> findByMember(Member member);
}