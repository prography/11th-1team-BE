package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.AppVersion;
import org.example.knockin.meta.entity.PlatformType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    List<AppVersion> findByPlatformType(PlatformType platformType, Pageable pageable);
    List<AppVersion> findByPlatformTypeOrderByCreatedAtDesc(PlatformType platformType);
}
