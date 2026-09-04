package org.example.knockin.global.auth.util;

import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class AppleOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private final RestClientAuthorizationCodeTokenResponseClient delegate = new RestClientAuthorizationCodeTokenResponseClient();

    public AppleOAuth2AccessTokenResponseClient(AppleClientSecretGenerator appleClientSecretGenerator) {
        delegate.addParametersConverter(grantRequest -> {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            if (OAuth2UserInfoProvider.APPLE.getRegistrationId().equalsIgnoreCase(grantRequest.getClientRegistration().getRegistrationId())) {
                String clientId = grantRequest.getClientRegistration().getClientId();
                String clientSecretJwt = appleClientSecretGenerator.createClientSecret(clientId);
                map.add("client_secret", clientSecretJwt);
            }
            return map;
        });
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        return delegate.getTokenResponse(authorizationGrantRequest);
    }
}
