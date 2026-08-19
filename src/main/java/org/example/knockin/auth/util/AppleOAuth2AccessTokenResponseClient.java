package org.example.knockin.auth.util;

import java.util.HashMap;
import java.util.Map;
import org.example.knockin.dto.AppleUserInfo;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

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
        OAuth2AccessTokenResponse tokenResponse = delegate.getTokenResponse(authorizationGrantRequest);
        return mergeAppleCallbackUserInfo(authorizationGrantRequest, tokenResponse);
    }

    OAuth2AccessTokenResponse mergeAppleCallbackUserInfo(
            OAuth2AuthorizationCodeGrantRequest grantRequest,
            OAuth2AccessTokenResponse tokenResponse) {
        if (!OAuth2UserInfoProvider.APPLE.getRegistrationId().equalsIgnoreCase(
                grantRequest.getClientRegistration().getRegistrationId())) {
            return tokenResponse;
        }

        OAuth2AuthorizationRequest authorizationRequest = grantRequest
                .getAuthorizationExchange()
                .getAuthorizationRequest();
        Map<String, Object> additionalParameters = new HashMap<>(
                tokenResponse.getAdditionalParameters());
        copyIfPresent(authorizationRequest, additionalParameters, AppleUserInfo.NAME_PARAMETER);
        copyIfPresent(authorizationRequest, additionalParameters, AppleUserInfo.EMAIL_PARAMETER);

        return OAuth2AccessTokenResponse.withResponse(tokenResponse)
                .additionalParameters(additionalParameters)
                .build();
    }

    private void copyIfPresent(
            OAuth2AuthorizationRequest authorizationRequest,
            Map<String, Object> additionalParameters,
            String key) {
        String value = authorizationRequest.getAttribute(key);
        if (StringUtils.hasText(value)) {
            additionalParameters.put(key, value);
        }
    }
}
