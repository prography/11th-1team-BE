package org.example.knockin.life.repository;

import org.example.knockin.life.entity.MemberLifePattern;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberLifePatternRepository extends JpaRepository<MemberLifePattern, Long>, MemberLifePatternRepositoryCustom {
    List<MemberLifePattern> findByMember(Member member);
}