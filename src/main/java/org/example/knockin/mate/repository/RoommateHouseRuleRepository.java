package org.example.knockin.mate.repository;

import java.util.List;
import org.example.knockin.mate.entity.RoommateHouseRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateHouseRuleRepository extends JpaRepository<RoommateHouseRule, Long>, RoommateHouseRuleRepositoryCustom {
    List<RoommateHouseRule> findByMyRoommateIdAndIsDeleted(Long myRoommateId, Boolean isDeleted);
}
