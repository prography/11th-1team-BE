package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.Region;

import java.util.List;

public interface RegionRepositoryCustom {
    List<Region> findByRegions(List<Long> regions);
    List<Region> findByIdInWithChild(List<Long> regionIds);
}