package org.example.knockin.life.repository;

import java.util.List;
import org.example.knockin.life.entity.MemberLifePatternLog;

public interface MemberLifePatternLogRepositoryCustom {
    List<MemberLifePatternLog> findLatestLogsWithFetchByMemberId(Long memberId);
}
