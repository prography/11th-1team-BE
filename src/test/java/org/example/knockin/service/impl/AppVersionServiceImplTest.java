package org.example.knockin.service.impl;

import org.example.knockin.meta.dto.AppVersionListDto;
import org.example.knockin.meta.dto.AppVersionModifyDto;
import org.example.knockin.meta.dto.AppVersionSaveDto;
import org.example.knockin.meta.entity.AppVersion;
import org.example.knockin.meta.entity.PlatformType;
import org.example.knockin.meta.entity.UpdateType;
import org.example.knockin.global.exception.AppVersionErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.meta.service.impl.AppVersionServiceImpl;
import org.example.knockin.meta.repository.AppVersionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("앱 버전 서비스 테스트")
class AppVersionServiceImplTest {

    @Mock
    private AppVersionRepository appVersionRepository;

    @InjectMocks
    private AppVersionServiceImpl appVersionService;

    @Test
    @DisplayName("앱 버전 조회 성공 테스트")
    void findAppVersionSuccessTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        AppVersion appVersion = AppVersion.builder()
                .id(1L)
                .version("1.0.0")
                .minVersion("1.0.0")
                .platformType(PlatformType.IOS)
                .updateType(UpdateType.SELECT)
                .isDeleted(false)
                .build();
        given(appVersionRepository.findAll(pageable)).willReturn(new PageImpl<>(List.of(appVersion)));

        // when
        AppVersionListDto.Response response = appVersionService.findAppVersion(pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getVersionInfo()).hasSize(1);
        assertThat(response.getVersionInfo().get(0).getId()).isEqualTo(1L);
        assertThat(response.getVersionInfo().get(0).getVersion()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("앱 버전 등록 성공 테스트")
    void saveAppVersionSuccessTest() {
        // given
        AppVersionSaveDto.Request request = new AppVersionSaveDto.Request();
        request.setVersion("2.0.0");
        request.setMinVersion("1.0.0");
        request.setPlatformType(PlatformType.IOS);
        request.setUpdateType(UpdateType.SELECT);

        // when
        AppVersionSaveDto.Response response = appVersionService.saveAppVersion(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(appVersionRepository).save(any(AppVersion.class));
    }

    @Test
    @DisplayName("앱 버전 수정 성공 테스트")
    void modifyAppVersionSuccessTest() {
        // given
        AppVersionModifyDto.Request request = new AppVersionModifyDto.Request();
        request.setId(1L);
        request.setVersion("1.0.1");
        request.setMinVersion("1.0.0");
        request.setPlatformType(PlatformType.IOS);
        request.setUpdateType(UpdateType.SELECT);

        AppVersion appVersion = spy(AppVersion.builder().id(1L).version("1.0.0").isDeleted(false).build());
        given(appVersionRepository.findById(1L)).willReturn(Optional.of(appVersion));

        // when
        AppVersionModifyDto.Response response = appVersionService.modifyAppVersion(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(appVersion).modifyVersion("1.0.1", "1.0.0", UpdateType.SELECT, PlatformType.IOS);
    }

    @Test
    @DisplayName("앱 버전 수정 시 버전 정보를 찾을 수 없으면 BusinessException 발생")
    void modifyAppVersionNotFoundTest() {
        // given
        AppVersionModifyDto.Request request = new AppVersionModifyDto.Request();
        request.setId(1L);
        request.setVersion("1.0.1");

        given(appVersionRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> appVersionService.modifyAppVersion(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AppVersionErrorCode.APP_VERSION_NOT_FOUND);

        verify(appVersionRepository, never()).save(any(AppVersion.class));
    }
}
