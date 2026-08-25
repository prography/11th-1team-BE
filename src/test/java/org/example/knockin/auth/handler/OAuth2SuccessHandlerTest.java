package org.example.knockin.auth.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.knockin.global.auth.handler.OAuth2SuccessHandler;
import org.example.knockin.global.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.example.knockin.global.auth.util.TokenProvider;
import org.example.knockin.meta.dto.AuthResponse;
import org.example.knockin.global.auth.dto.PrincipalDetails;
import org.example.knockin.authentication.entity.LoginProviderType;
import org.example.knockin.member.entity.Member;
import org.example.knockin.member.entity.MemberRole;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.KnockInProps;
import org.example.knockin.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@DisplayName("OAuth2 로그인 성공 핸들러")
class OAuth2SuccessHandlerTest {

    private static final String ACCESS_TOKEN = "knockin-access-token";

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private KnockInProps knockInProps;

    @Mock
    private MemberServiceImpl memberService;

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @Mock
    private Authentication authentication;

    private OAuth2SuccessHandler successHandler;
    private Member member;

    @BeforeEach
    void setUp() {
        successHandler = new OAuth2SuccessHandler(
                tokenProvider,
                knockInProps,
                memberService,
                authorizationRequestRepository
        );
        member = Member.builder()
                .id(1L)
                .providerType(LoginProviderType.KAKAO)
                .providerId("kakao-member")
                .role(MemberRole.USER)
                .isDelete(false)
                .build();
        when(authentication.getPrincipal()).thenReturn(new PrincipalDetails(member));
        when(tokenProvider.generateAccessToken(authentication)).thenReturn(ACCESS_TOKEN);
    }

    @Test
    @DisplayName("활성 회원의 SDK 로그인은 HTTP 200과 서비스 액세스 토큰을 반환한다")
    void sdkLoginReturnsAccessTokenForActiveMember() throws Exception {
        MockHttpServletRequest request = sdkLoginRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthResponse authResponse = authResponse(false, null, ACCESS_TOKEN);
        when(memberService.findMemberForLogin(member, ACCESS_TOKEN)).thenReturn(authResponse);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getContentAsString())
                .contains("\"status\":200")
                .contains("\"accessToken\":\"" + ACCESS_TOKEN + "\"")
                .contains("\"reason\":null");
        verify(memberService).findMemberForLogin(member, ACCESS_TOKEN);
    }

    @Test
    @DisplayName("정지 또는 탈퇴 회원의 SDK 로그인은 HTTP 401과 사유를 반환하고 서비스 토큰을 제거한다")
    void sdkLoginRejectsUnavailableMemberWithoutAccessToken() throws Exception {
        MockHttpServletRequest request = sdkLoginRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String reason = "신고 누적으로 이용이 정지되었습니다.";
        AuthResponse authResponse = authResponse(true, reason, ACCESS_TOKEN);
        when(memberService.findMemberForLogin(member, ACCESS_TOKEN)).thenReturn(authResponse);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertThat(response.getStatus())
                .isEqualTo(AuthErrorCode.MEMBER_IS_DELETE.getHttpStatus().value());
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getContentAsString())
                .contains("\"status\":401")
                .contains("\"accessToken\":null")
                .contains("\"delete\":true")
                .contains("\"reason\":\"" + reason + "\"");
        assertThat(authResponse.getAccessToken()).isNull();
        assertThat(response.getHeaders(HttpHeaders.SET_COOKIE)).isEmpty();
        verify(memberService).findMemberForLogin(member, ACCESS_TOKEN);
    }

    private MockHttpServletRequest sdkLoginRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("isSdkLogin", true);
        return request;
    }

    private AuthResponse authResponse(boolean delete, String reason, String accessToken) {
        AuthResponse.DeleteInfo deleteInfo = new AuthResponse.DeleteInfo();
        deleteInfo.setDelete(delete);
        deleteInfo.setReason(reason);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .basicInfo(true)
                .preferenceInfo(true)
                .deleteInfo(deleteInfo)
                .build();
    }
}
