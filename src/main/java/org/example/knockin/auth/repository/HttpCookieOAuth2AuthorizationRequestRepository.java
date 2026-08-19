package org.example.knockin.auth.repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.knockin.auth.util.CookieUtils;
import org.example.knockin.auth.util.OAuth2UserInfoProvider;
import org.example.knockin.dto.AppleUserInfo;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "target_url";
    public static final String INAPP_REDIRECT_URI_PARAM = "knockinrn://";
    private static final int cookieExpireSeconds = 180;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
        .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String targetUrlAfterLogin = request.getParameter("target_url");
        if (StringUtils.hasText(targetUrlAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, targetUrlAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
        if (authorizationRequest != null) {
            authorizationRequest = mergeAppleCallbackUserInfo(authorizationRequest, request.getParameter("user"));
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        }
        return authorizationRequest;
    }

    OAuth2AuthorizationRequest mergeAppleCallbackUserInfo(OAuth2AuthorizationRequest authorizationRequest, String userJson) {
        if (!isApple(authorizationRequest) || !StringUtils.hasText(userJson)) {
            return authorizationRequest;
        }

        try {
            Map<String, Object> user = objectMapper.readValue(userJson, Map.class);
            Object nameValue = user.get("name");
            Map<?, ?> name = nameValue instanceof Map<?, ?> map ? map : Map.of();
            String firstName = stringValue(name.get("firstName"));
            String lastName = stringValue(name.get("lastName"));
            String fullName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
            String email = stringValue(user.get("email"));

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .attributes(attributes -> {
                        if (StringUtils.hasText(fullName)) {
                            attributes.put(AppleUserInfo.NAME_PARAMETER, fullName);
                        }
                        if (StringUtils.hasText(email)) {
                            attributes.put(AppleUserInfo.EMAIL_PARAMETER, email);
                        }
                    })
                    .build();
        } catch (Exception e) {
            log.warn("Apple 사용자 정보 파싱 실패: {}", e.getMessage());
            return authorizationRequest;
        }
    }

    private boolean isApple(OAuth2AuthorizationRequest authorizationRequest) {
        String registrationId = authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
        return OAuth2UserInfoProvider.APPLE.getRegistrationId().equalsIgnoreCase(registrationId);
    }

    private String stringValue(Object value) {
        return value instanceof String text ? text : null;
    }

    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
