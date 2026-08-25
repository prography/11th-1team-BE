package org.example.knockin.matching.repository;

import java.util.List;
import org.example.knockin.matching.entity.ChattingScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingScoreRepository extends JpaRepository<ChattingScore, Long>, ChattingScoreRepositoryCustom {
    List<ChattingScore> findByChattingRequiredId(Long chattingRequiredId);
}