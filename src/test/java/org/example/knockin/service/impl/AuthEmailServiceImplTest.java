package org.example.knockin.service.impl;

import org.example.knockin.meta.dto.AuthEmailDeleteDto;
import org.example.knockin.meta.dto.AuthEmailListDto;
import org.example.knockin.meta.dto.AuthEmailModifyDto;
import org.example.knockin.meta.dto.AuthEmailSaveDto;
import org.example.knockin.verification.entity.AuthenticationType;
import org.example.knockin.meta.entity.AuthEmail;
import org.example.knockin.global.exception.AuthEmailErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.meta.service.impl.AuthEmailServiceImpl;
import org.example.knockin.meta.repository.AuthEmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 가능 이메일 도메인 설정 서비스 테스트")
class AuthEmailServiceImplTest {

    @Mock
    private AuthEmailRepository authEmailRepository;

    @InjectMocks
    private AuthEmailServiceImpl authEmailService;

    @Test
    @DisplayName("인증 가능 이메일 도메인 목록 조회 성공 테스트 (isDeleted=false 필터링)")
    void findAuthEmailListSuccessTest() {
        // given
        AuthEmail email1 = AuthEmail.builder()
                .id(1L)
                .domain("univ.ac.kr")
                .name("대학교 이메일")
                .dtype(AuthenticationType.STUDENT)
                .isDeleted(false)
                .build();
        AuthEmail email2 = AuthEmail.builder()
                .id(2L)
                .domain("company.com")
                .name("직장 이메일")
                .dtype(AuthenticationType.COMPANY)
                .isDeleted(false)
                .build();

        given(authEmailRepository.findByIsDeletedFalse()).willReturn(List.of(email1, email2));

        // when
        AuthEmailListDto.Response response = authEmailService.findAuthEmailList();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAuthEmailInfoList()).hasSize(2);

        AuthEmailListDto.Response.AuthEmailInfo info1 = response.getAuthEmailInfoList().get(0);
        assertThat(info1.getId()).isEqualTo(1L);
        assertThat(info1.getDomain()).isEqualTo("univ.ac.kr");

        AuthEmailListDto.Response.AuthEmailInfo info2 = response.getAuthEmailInfoList().get(1);
        assertThat(info2.getId()).isEqualTo(2L);
        assertThat(info2.getDomain()).isEqualTo("company.com");
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 등록 성공 테스트")
    void saveAuthEmailSuccessTest() {
        // given
        AuthEmailSaveDto.Request request = new AuthEmailSaveDto.Request();
        request.setDomain("new-univ.ac.kr");
        request.setName("신규 대학교");
        request.setType(AuthenticationType.STUDENT);

        given(authEmailRepository.existsByDomainAndIsDeletedFalse("new-univ.ac.kr")).willReturn(false);

        // when
        AuthEmailSaveDto.Response response = authEmailService.saveAuthEmail(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(authEmailRepository).save(any(AuthEmail.class));
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 등록 시 중복 도메인이면 BusinessException 발생")
    void saveAuthEmailDuplicateTest() {
        // given
        AuthEmailSaveDto.Request request = new AuthEmailSaveDto.Request();
        request.setDomain("univ.ac.kr");
        request.setName("대학교");
        request.setType(AuthenticationType.STUDENT);

        given(authEmailRepository.existsByDomainAndIsDeletedFalse("univ.ac.kr")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authEmailService.saveAuthEmail(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthEmailErrorCode.AUTH_EMAIL_DUPLICATE_DOMAIN);

        verify(authEmailRepository, never()).save(any(AuthEmail.class));
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 수정 성공 테스트")
    void modifyAuthEmailSuccessTest() {
        // given
        AuthEmailModifyDto.Request request = new AuthEmailModifyDto.Request();
        request.setId(1L);
        request.setDomain("modified-univ.ac.kr");
        request.setName("변경된 대학교");
        request.setType(AuthenticationType.STUDENT);

        AuthEmail authEmail = spy(AuthEmail.builder()
                .id(1L)
                .domain("univ.ac.kr")
                .name("대학교 이메일")
                .dtype(AuthenticationType.STUDENT)
                .isDeleted(false)
                .build());

        given(authEmailRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(authEmail));
        given(authEmailRepository.existsByDomainAndIsDeletedFalseAndIdNot("modified-univ.ac.kr", 1L)).willReturn(false);

        // when
        AuthEmailModifyDto.Response response = authEmailService.modifyAuthEmail(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(authEmail).modifyAuthEmail(request);
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 수정 시 중복 도메인이면 BusinessException 발생")
    void modifyAuthEmailDuplicateTest() {
        // given
        AuthEmailModifyDto.Request request = new AuthEmailModifyDto.Request();
        request.setId(1L);
        request.setDomain("duplicate-univ.ac.kr");
        request.setName("변경된 대학교");
        request.setType(AuthenticationType.STUDENT);

        AuthEmail authEmail = AuthEmail.builder()
                .id(1L)
                .domain("univ.ac.kr")
                .name("대학교 이메일")
                .dtype(AuthenticationType.STUDENT)
                .isDeleted(false)
                .build();

        given(authEmailRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(authEmail));
        given(authEmailRepository.existsByDomainAndIsDeletedFalseAndIdNot("duplicate-univ.ac.kr", 1L)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authEmailService.modifyAuthEmail(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthEmailErrorCode.AUTH_EMAIL_DUPLICATE_DOMAIN);
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 수정 시 대상을 찾을 수 없으면 BusinessException 발생")
    void modifyAuthEmailNotFoundTest() {
        // given
        AuthEmailModifyDto.Request request = new AuthEmailModifyDto.Request();
        request.setId(1L);
        request.setDomain("modified-univ.ac.kr");
        request.setName("변경된 대학교");
        request.setType(AuthenticationType.STUDENT);

        given(authEmailRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authEmailService.modifyAuthEmail(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthEmailErrorCode.AUTH_EMAIL_NOT_FOUND);

        verify(authEmailRepository, never()).save(any(AuthEmail.class));
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 삭제 성공 테스트")
    void deleteAuthEmailSuccessTest() {
        // given
        AuthEmail authEmail = AuthEmail.builder()
                .id(1L)
                .domain("univ.ac.kr")
                .name("대학교 이메일")
                .dtype(AuthenticationType.STUDENT)
                .isDeleted(false)
                .build();

        given(authEmailRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(authEmail));

        // when
        AuthEmailDeleteDto.Response response = authEmailService.deleteAuthEmail(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        assertThat(authEmail.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("인증 가능 이메일 도메인 삭제 시 대상을 찾을 수 없으면 BusinessException 발생")
    void deleteAuthEmailNotFoundTest() {
        // given
        given(authEmailRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authEmailService.deleteAuthEmail(1L))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthEmailErrorCode.AUTH_EMAIL_NOT_FOUND);
    }
}
