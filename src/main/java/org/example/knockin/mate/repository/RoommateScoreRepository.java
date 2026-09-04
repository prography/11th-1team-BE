package org.example.knockin.mate.repository;

import org.example.knockin.mate.entity.RoommateScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateScoreRepository extends JpaRepository<RoommateScore, Long>, RoommateScoreRepositoryCustom {
}
