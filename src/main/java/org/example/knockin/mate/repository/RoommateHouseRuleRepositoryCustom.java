package org.example.knockin.mate.repository;

import java.util.Optional;
import org.example.knockin.mate.entity.RoommateHouseRule;

public interface RoommateHouseRuleRepositoryCustom {
    Optional<RoommateHouseRule> findWithFetchedById(Long id);
}
