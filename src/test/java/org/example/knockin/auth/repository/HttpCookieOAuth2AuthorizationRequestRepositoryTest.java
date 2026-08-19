package org.example.knockin.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.example.knockin.dto.AppleUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

class HttpCookieOAuth2AuthorizationRequestRepositoryTest {

    private final HttpCookieOAuth2AuthorizationRequestRepository repository =
            new HttpCookieOAuth2AuthorizationRequestRepository();

    @Test
    @DisplayName("Apple 최초 웹 콜백의 user.name과 user.email을 authorization request에 병합한다")
    void mergesAppleCallbackUserParameter() {
        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest
                .authorizationCode()
                .authorizationUri("https://appleid.apple.com/auth/authorize")
                .clientId("client-id")
                .redirectUri("https://example.com/login/oauth2/code/apple")
                .scopes(Set.of("name", "email"))
                .state("state")
                .attributes(attributes ->
                        attributes.put(OAuth2ParameterNames.REGISTRATION_ID, "apple"))
                .build();

        OAuth2AuthorizationRequest merged = repository.mergeAppleCallbackUserInfo(
                authorizationRequest,
                """
                {
                  "name": {"firstName": "Apple", "lastName": "User"},
                  "email": "apple@example.com"
                }
                """);

        assertThat(merged.<String>getAttribute(AppleUserInfo.NAME_PARAMETER))
                .isEqualTo("Apple User");
        assertThat(merged.<String>getAttribute(AppleUserInfo.EMAIL_PARAMETER))
                .isEqualTo("apple@example.com");
    }
}
