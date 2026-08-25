package org.example.knockin.service.impl;

import org.example.knockin.life.service.impl.LifeStyleServiceImpl;
import org.example.knockin.life.dto.BoLifeStylePatternDetailDto;
import org.example.knockin.life.dto.BoLifeStylePatternListDto;
import org.example.knockin.life.entity.LifePattern;
import org.example.knockin.life.entity.LifePatternInformation;
import org.example.knockin.life.entity.LifePatternType;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.LifePatternErrorCode;
import org.example.knockin.life.repository.LifePatternInformationRepository;
import org.example.knockin.life.repository.LifePatternRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
@DisplayName("라이프스타일 패턴 관리 서비스 테스트")
class LifeStyleServiceImplTest {

    @Mock
    private LifePatternRepository lifePatternRepository;

    @Mock
    private LifePatternInformationRepository lifePatternInformationRepository;

    @InjectMocks
    private LifeStyleServiceImpl lifeStyleService;

    @Test
    @DisplayName("라이프스타일 패턴 등록 성공 테스트")
    void saveLifePatternSuccessTest() {
        // given
        LifePattern lifePattern = LifePattern.builder().name("흡연 여부").dtype(LifePatternType.BOOLEAN).sort(1).build();
        given(lifePatternRepository.save(lifePattern)).willReturn(lifePattern);

        // when
        LifePattern result = lifeStyleService.saveLifePattern(lifePattern);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("흡연 여부");
        verify(lifePatternRepository).save(lifePattern);
    }

    @Test
    @DisplayName("라이프스타일 패턴 상세 목록 등록 성공 테스트")
    void saveLifePatternInformationSuccessTest() {
        // given
        List<LifePatternInformation> list = List.of(
                LifePatternInformation.builder().dvalue("흡연").description("담배를 피움").build(),
                LifePatternInformation.builder().dvalue("비흡연").description("담배를 피우지 않음").build()
        );
        given(lifePatternInformationRepository.saveAll(list)).willReturn(list);

        // when
        List<LifePatternInformation> result = lifeStyleService.saveLifePatternInformation(list);

        // then
        assertThat(result).hasSize(2);
        verify(lifePatternInformationRepository).saveAll(list);
    }

    @Test
    @DisplayName("라이프스타일 패턴 상세 단건 등록 성공 테스트")
    void saveLifeInformationSuccessTest() {
        // given
        LifePatternInformation info = LifePatternInformation.builder().dvalue("흡연").description("담배를 피움").build();
        given(lifePatternInformationRepository.save(info)).willReturn(info);

        // when
        LifePatternInformation result = lifeStyleService.saveLifeInformation(info);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDvalue()).isEqualTo("흡연");
        verify(lifePatternInformationRepository).save(info);
    }

    @Test
    @DisplayName("라이프스타일 패턴 삭제 성공 테스트")
    void deleteLifePatternSuccessTest() {
        // given
        Long id = 1L;
        LifePattern existing = spy(LifePattern.builder().id(id).name("흡연 여부").isDeleted(false).build());
        given(lifePatternRepository.findById(id)).willReturn(Optional.of(existing));

        // when
        LifePattern result = lifeStyleService.deleteLifePattern(id);

        // then
        assertThat(result).isNotNull();
        verify(existing).deleteLifePattern();
        assertThat(result.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("라이프스타일 패턴 삭제 시 대상을 찾을 수 없으면 BusinessException 발생")
    void deleteLifePatternNotFoundTest() {
        // given
        Long id = 1L;
        given(lifePatternRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lifeStyleService.deleteLifePattern(id))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", LifePatternErrorCode.LIFE_PATTERN_NOT_FOUNT);
    }

    @Test
    @DisplayName("패턴 기준 패턴 상세 삭제 성공 테스트")
    void deleteLifeInformationByPatternSuccessTest() {
        // given
        LifePattern pattern = LifePattern.builder().id(1L).build();

        // when
        lifeStyleService.deleteLifeInformationByPattern(pattern);

        // then
        verify(lifePatternInformationRepository).deleteByLifePattern(pattern);
    }

    @Test
    @DisplayName("라이프스타일 패턴 페이징 목록 조회 성공 테스트")
    void findLifeStylePatternListSuccessTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        BoLifeStylePatternListDto.Response.PatternItem item = BoLifeStylePatternListDto.Response.PatternItem.builder()
                .id(1L)
                .name("흡연 여부")
                .build();
        BoLifeStylePatternListDto.Response expected = BoLifeStylePatternListDto.Response.builder().patterns(List.of(item)).build();

        given(lifePatternRepository.findLifeStylePatternList(pageable)).willReturn(List.of(item));

        // when
        BoLifeStylePatternListDto.Response result = lifeStyleService.findLifeStylePatternList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPatterns()).hasSize(1);
        assertThat(result.getPatterns().get(0).getName()).isEqualTo("흡연 여부");
    }

    @Test
    @DisplayName("라이프스타일 패턴 상세 조회 성공 테스트")
    void findLifeStylePatternSuccessTest() {
        // given
        Long id = 1L;
        BoLifeStylePatternDetailDto.Response expected = BoLifeStylePatternDetailDto.Response.builder().id(id).name("흡연 여부").build();

        given(lifePatternRepository.findLifeStylePattern(id)).willReturn(expected);

        // when
        BoLifeStylePatternDetailDto.Response result = lifeStyleService.findLifeStylePattern(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("흡연 여부");
    }

    @Test
    @DisplayName("라이프스타일 패턴 단건 엔티티 조회 성공 테스트")
    void findLifeStyleSuccessTest() {
        // given
        Long id = 1L;
        LifePattern pattern = LifePattern.builder().id(id).name("흡연 여부").build();
        given(lifePatternRepository.findById(id)).willReturn(Optional.of(pattern));

        // when
        LifePattern result = lifeStyleService.findLifeStyle(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("라이프스타일 패턴 단건 엔티티 조회 시 대상을 찾을 수 없으면 BusinessException 발생")
    void findLifeStyleNotFoundTest() {
        // given
        Long id = 1L;
        given(lifePatternRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lifeStyleService.findLifeStyle(id))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", LifePatternErrorCode.LIFE_PATTERN_NOT_FOUNT);
    }
}
