package org.example.knockin.life.repository;

import org.example.knockin.life.entity.MemberLifePatternLogDegree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLifePatternLogDegreeRepository extends JpaRepository<MemberLifePatternLogDegree, Long>, MemberLifePatternLogDegreeRepositoryCustom {
}