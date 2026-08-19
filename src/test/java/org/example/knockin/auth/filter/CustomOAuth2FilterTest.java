package org.example.knockin.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.example.knockin.auth.handler.OAuth2FailureHandler;
import org.example.knockin.auth.handler.OAuth2SuccessHandler;
import org.example.knockin.auth.service.CustomOAuth2UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2FilterTest {

    @Mock
    private ClientRegistrationRepository clientRegistrationRepository;
    @Mock
    private CustomOAuth2UserService oAuth2UserService;
    @Mock
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Mock
    private OAuth2FailureHandler oAuth2FailureHandler;

    private CustomOAuth2Filter filter;

    @BeforeEach
    void setUp() {
        filter = new CustomOAuth2Filter(
                clientRegistrationRepository,
                oAuth2UserService,
                oAuth2SuccessHandler,
                oAuth2FailureHandler);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Apple SDK 이름과 이메일을 명시적 사용자 로딩 인자로 전달한다")
    void passesAppleSdkUserInfoExplicitly() throws Exception {
        ClientRegistration registration = appleRegistration();
        given(clientRegistrationRepository.findByRegistrationId("apple")).willReturn(registration);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        given(oAuth2User.getAuthorities()).willReturn(List.of());
        given(oAuth2UserService.loadAppleUser(
                any(OAuth2UserRequest.class),
                eq("Apple User"),
                eq("apple@example.com"))).willReturn(oAuth2User);

        MockHttpServletRequest request = new MockHttpServletRequest(
                "POST", "/sdk/oauth2/authorization/apple");
        request.setServletPath("/sdk/oauth2/authorization/apple");
        request.setContentType("application/json");
        request.setContent("""
                {"authObj":{"access_token":"identity-token","name":"Apple User","email":"apple@example.com"}}
                """.getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean();

        filter.doFilter(request, response, (servletRequest, servletResponse) ->
                chainCalled.set(true));

        verify(oAuth2UserService).loadAppleUser(
                any(OAuth2UserRequest.class),
                eq("Apple User"),
                eq("apple@example.com"));
        verify(oAuth2SuccessHandler).onAuthenticationSuccess(any(), any(), any());
        assertThat(chainCalled).isFalse();
    }

    private ClientRegistration appleRegistration() {
        return ClientRegistration.withRegistrationId("apple")
                .clientId("client-id")
                .clientSecret("client-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("https://example.com/login/oauth2/code/apple")
                .authorizationUri("https://appleid.apple.com/auth/authorize")
                .tokenUri("https://appleid.apple.com/auth/token")
                .userNameAttributeName("sub")
                .clientName("Apple")
                .build();
    }
}
