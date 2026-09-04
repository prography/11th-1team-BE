package org.example.knockin.meta.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.dto.AppVersionDto;
import org.example.knockin.meta.dto.AppVersionListDto;
import org.example.knockin.meta.dto.AppVersionModifyDto;
import org.example.knockin.meta.dto.AppVersionSaveDto;
import org.example.knockin.meta.entity.AppVersion;
import org.example.knockin.meta.entity.PlatformType;
import org.example.knockin.global.exception.AppVersionErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.meta.repository.AppVersionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl {
    private final AppVersionRepository appVersionRepository;

    public AppVersionListDto.Response findAppVersion(Pageable pageable) {
        List<AppVersionListDto.Response.VersionInfo> appVersion = appVersionRepository.findAll(pageable).stream().map(item ->
                AppVersionListDto.Response.VersionInfo.builder()
                        .id(item.getId()).version(item.getVersion()).minVersion(item.getMinVersion())
                        .createdAt(item.getCreatedAt()).platformType(item.getPlatformType())
                        .updateType(item.getUpdateType()).build()).toList();
        return AppVersionListDto.Response.builder().versionInfo(appVersion).build();
    }

    public AppVersionListDto.Response findAppIosVersion(Pageable pageable) {
        List<AppVersionListDto.Response.VersionInfo> appVersion = appVersionRepository.findByPlatformType(PlatformType.IOS, pageable).stream().map(item ->
                AppVersionListDto.Response.VersionInfo.builder()
                        .id(item.getId()).version(item.getVersion()).minVersion(item.getMinVersion())
                        .createdAt(item.getCreatedAt()).platformType(item.getPlatformType())
                        .updateType(item.getUpdateType()).build()).toList();
        return AppVersionListDto.Response.builder().versionInfo(appVersion).build();
    }

    public AppVersionListDto.Response findAppAndroidVersion(Pageable pageable) {
        List<AppVersionListDto.Response.VersionInfo> appVersion = appVersionRepository.findByPlatformType(PlatformType.ANDROID, pageable).stream().map(item ->
                AppVersionListDto.Response.VersionInfo.builder()
                        .id(item.getId()).version(item.getVersion()).minVersion(item.getMinVersion())
                        .createdAt(item.getCreatedAt()).platformType(item.getPlatformType())
                        .updateType(item.getUpdateType()).build()).toList();
        return AppVersionListDto.Response.builder().versionInfo(appVersion).build();
    }

    public AppVersionDto.Response findAppAndroidVersionLatest() {
        AppVersion appVersion = appVersionRepository.findByPlatformTypeOrderByCreatedAtDesc(PlatformType.ANDROID).stream().findFirst().orElse(null);
        if (appVersion == null) {
            return AppVersionDto.Response.builder().build();
        }
        return AppVersionDto.Response.builder().id(appVersion.getId()).version(appVersion.getVersion()).minVersion(appVersion.getMinVersion()).createdAt(appVersion.getCreatedAt()).platformType(appVersion.getPlatformType()).updateType(appVersion.getUpdateType()).build();
    }

    public AppVersionDto.Response findAppIosVersionLatest() {
        AppVersion appVersion = appVersionRepository.findByPlatformTypeOrderByCreatedAtDesc(PlatformType.IOS).stream().findFirst().orElse(null);
        if (appVersion == null) {
            return AppVersionDto.Response.builder().build();
        }
        return AppVersionDto.Response.builder().id(appVersion.getId()).version(appVersion.getVersion()).minVersion(appVersion.getMinVersion()).createdAt(appVersion.getCreatedAt()).platformType(appVersion.getPlatformType()).updateType(appVersion.getUpdateType()).build();
    }

    @Transactional
    public AppVersionSaveDto.Response saveAppVersion(AppVersionSaveDto.Request request) {
        appVersionRepository.save(AppVersion.builder().version(request.getVersion()).platformType(request.getPlatformType()).updateType(request.getUpdateType()).minVersion(request.getMinVersion()).build());
        return AppVersionSaveDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public AppVersionModifyDto.Response modifyAppVersion(AppVersionModifyDto.Request request) {
        AppVersion appVersion = appVersionRepository.findById(request.getId()).orElseThrow(() -> new BusinessException(AppVersionErrorCode.APP_VERSION_NOT_FOUND));
        appVersion.modifyVersion(request.getVersion(), request.getMinVersion(), request.getUpdateType(), request.getPlatformType());
        return AppVersionModifyDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }
}
