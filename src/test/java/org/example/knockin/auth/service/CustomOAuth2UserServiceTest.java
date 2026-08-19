package org.example.knockin.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import org.example.knockin.dto.AppleUserInfo;
import org.example.knockin.dto.OAuth2UserInfo;
import org.example.knockin.entity.auth.LoginProviderType;
import org.example.knockin.entity.member.Member;
import org.example.knockin.entity.member.MemberRole;
import org.example.knockin.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("Apple 웹 콜백 정보와 id_token claims를 AppleUserInfo로 병합해 회원 서비스에 전달한다")
    void mergesAppleWebCallbackUserInfoAndPassesItToMemberService() {
        CustomOAuth2UserService service = new CustomOAuth2UserService(memberService);
        given(memberService.getOrSave(any(OAuth2UserInfo.class))).willReturn(appleMember());

        OAuth2UserRequest userRequest = new OAuth2UserRequest(
                appleRegistration(),
                accessToken("web-access-token"),
                Map.of(
                        "id_token", jwt(Map.of("sub", "apple-web-id")),
                        AppleUserInfo.NAME_PARAMETER, "Web Apple",
                        AppleUserInfo.EMAIL_PARAMETER, "web@apple.example"));

        service.loadUser(userRequest);

        OAuth2UserInfo userInfo = capturedUserInfo();
        assertThat(userInfo).isInstanceOf(AppleUserInfo.class);
        assertThat(userInfo.getId()).isEqualTo("apple-web-id");
        assertThat(userInfo.getName()).isEqualTo("Web Apple");
        assertThat(userInfo.getEmail()).isEqualTo("web@apple.example");
        assertThat(userInfo.getProviderType()).isEqualTo(LoginProviderType.APPLE);
    }

    @Test
    @DisplayName("Apple SDK fullName/email과 identityToken claims를 AppleUserInfo로 병합해 회원 서비스에 전달한다")
    void mergesAppleSdkUserInfoAndPassesItToMemberService() {
        CustomOAuth2UserService service = new CustomOAuth2UserService(memberService);
        given(memberService.getOrSave(any(OAuth2UserInfo.class))).willReturn(appleMember());
        OAuth2UserRequest userRequest = new OAuth2UserRequest(
                appleRegistration(),
                accessToken(jwt(Map.of("sub", "apple-sdk-id"))));

        service.loadAppleUser(userRequest, "SDK Apple", "sdk@apple.example");

        OAuth2UserInfo userInfo = capturedUserInfo();
        assertThat(userInfo).isInstanceOf(AppleUserInfo.class);
        assertThat(userInfo.getId()).isEqualTo("apple-sdk-id");
        assertThat(userInfo.getName()).isEqualTo("SDK Apple");
        assertThat(userInfo.getEmail()).isEqualTo("sdk@apple.example");
        assertThat(userInfo.getProviderType()).isEqualTo(LoginProviderType.APPLE);
    }

    @Test
    @DisplayName("Apple id_token의 이메일이 있으면 callback 이메일로 덮어쓰지 않는다")
    void preservesAppleIdentityTokenEmailWhenMerging() {
        CustomOAuth2UserService service = new CustomOAuth2UserService(memberService);
        given(memberService.getOrSave(any(OAuth2UserInfo.class))).willReturn(appleMember());
        OAuth2UserRequest userRequest = new OAuth2UserRequest(
                appleRegistration(),
                accessToken(jwt(Map.of(
                        "sub", "apple-sdk-id",
                        "email", "claims@apple.example"))));

        service.loadAppleUser(userRequest, "SDK Apple", "client@apple.example");

        assertThat(capturedUserInfo().getEmail()).isEqualTo("claims@apple.example");
    }

    private OAuth2UserInfo capturedUserInfo() {
        ArgumentCaptor<OAuth2UserInfo> captor = ArgumentCaptor.forClass(OAuth2UserInfo.class);
        verify(memberService).getOrSave(captor.capture());
        return captor.getValue();
    }

    private Member appleMember() {
        return Member.builder()
                .id(1L)
                .providerType(LoginProviderType.APPLE)
                .providerId("apple-provider-id")
                .role(MemberRole.USER)
                .build();
    }

    private OAuth2AccessToken accessToken(String value) {
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                value,
                Instant.now(),
                Instant.now().plusSeconds(60));
    }

    private String jwt(Map<String, Object> claims) {
        String json = new tools.jackson.databind.ObjectMapper().writeValueAsString(claims);
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                json.getBytes(StandardCharsets.UTF_8));
        return "header." + payload + ".signature";
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
