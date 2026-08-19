package org.example.knockin.auth.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import org.example.knockin.dto.AppleUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

class AppleOAuth2AccessTokenResponseClientTest {

    @Test
    @DisplayName("Apple callback 정보를 OAuth2UserRequest로 이어질 token additional parameters에 전달한다")
    void passesAppleCallbackUserInfoThroughTokenResponse() {
        AppleOAuth2AccessTokenResponseClient client =
                new AppleOAuth2AccessTokenResponseClient(mock(AppleClientSecretGenerator.class));
        ClientRegistration registration = appleRegistration();
        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest
                .authorizationCode()
                .authorizationUri("https://appleid.apple.com/auth/authorize")
                .clientId("client-id")
                .redirectUri("https://example.com/login/oauth2/code/apple")
                .scopes(Set.of("name", "email"))
                .state("state")
                .attributes(attributes -> {
                    attributes.put(OAuth2ParameterNames.REGISTRATION_ID, "apple");
                    attributes.put(AppleUserInfo.NAME_PARAMETER, "Apple User");
                    attributes.put(AppleUserInfo.EMAIL_PARAMETER, "apple@example.com");
                })
                .build();
        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse
                .success("code")
                .redirectUri("https://example.com/login/oauth2/code/apple")
                .state("state")
                .build();
        OAuth2AuthorizationCodeGrantRequest grantRequest =
                new OAuth2AuthorizationCodeGrantRequest(
                        registration,
                        new OAuth2AuthorizationExchange(
                                authorizationRequest, authorizationResponse));
        OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse
                .withToken("access-token")
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(3600)
                .additionalParameters(Map.of("id_token", "identity-token"))
                .build();

        OAuth2AccessTokenResponse merged =
                client.mergeAppleCallbackUserInfo(grantRequest, tokenResponse);

        assertThat(merged.getAdditionalParameters())
                .containsEntry("id_token", "identity-token")
                .containsEntry(AppleUserInfo.NAME_PARAMETER, "Apple User")
                .containsEntry(AppleUserInfo.EMAIL_PARAMETER, "apple@example.com");
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
