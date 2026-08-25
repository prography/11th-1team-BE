package org.example.knockin.matching.repository;

import org.example.knockin.matching.entity.RoommateScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateScoreRepository extends JpaRepository<RoommateScore, Long>, RoommateScoreRepositoryCustom {
}
