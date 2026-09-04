package org.example.knockin.meta.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.entity.Region;
import org.example.knockin.meta.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl {
    private final RegionRepository regionRepository;

    public List<Region> findByIdInWithChild(List<Long> regionIds) {
        return regionRepository.findByIdInWithChild(regionIds);
    }

    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    public List<Region> findByRegions(List<Long> regions) {
        return regionRepository.findByRegions(regions);
    }

    public List<Region> findAll() { return regionRepository.findAll(); }
}
