package org.example.knockin.life.repository;

import java.util.List;
import org.example.knockin.life.entity.MemberLifePatternLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLifePatternLogRepository extends JpaRepository<MemberLifePatternLog, Long>, MemberLifePatternLogRepositoryCustom {
    List<MemberLifePatternLog> findByMemberId(Long memberId);
}