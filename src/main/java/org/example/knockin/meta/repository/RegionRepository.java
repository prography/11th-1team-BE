package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long>, RegionRepositoryCustom {
}