package org.example.knockin.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.knockin.entity.member.Member;
import org.example.knockin.auth.util.OAuth2UserInfoProvider;
import org.example.knockin.dto.AppleUserInfo;
import org.example.knockin.dto.OAuth2UserInfo;
import org.example.knockin.dto.PrincipalDetails;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.exception.AuthException;
import org.example.knockin.service.impl.MemberServiceImpl;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberServiceImpl memberService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String JWT_DELIMITER_REGEX = "\\.";

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (OAuth2UserInfoProvider.APPLE.getRegistrationId().equalsIgnoreCase(registrationId)) {
            return loadAppleUser(userRequest, additionalParameter(userRequest, AppleUserInfo.NAME_PARAMETER), additionalParameter(userRequest, AppleUserInfo.EMAIL_PARAMETER));
        }

        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        Class<? extends OAuth2UserInfo> infoClass = OAuth2UserInfoProvider
                .findByRegistrationId(registrationId)
                .getInfoClass();
        OAuth2UserInfo userInfo = objectMapper.convertValue(attributes, infoClass);
        return createPrincipal(userRequest, attributes, userInfo);
    }

    @Transactional
    public OAuth2User loadAppleUser(OAuth2UserRequest userRequest, String name, String email) {
        String idToken = additionalParameter(userRequest, "id_token");
        if (idToken == null || idToken.isBlank()) {
            idToken = userRequest.getAccessToken().getTokenValue();
        }

        Map<String, Object> claims = decodeJwtPayload(idToken);
        AppleUserInfo userInfo = objectMapper.convertValue(claims, AppleUserInfo.class);
        userInfo.merge(name, email);

        Map<String, Object> attributes = new HashMap<>(claims);
        if (userInfo.getName() != null) {
            attributes.put("name", userInfo.getName());
        }
        if (userInfo.getEmail() != null) {
            attributes.put("email", userInfo.getEmail());
        }
        return createPrincipal(userRequest, attributes, userInfo);
    }

    private OAuth2User createPrincipal(OAuth2UserRequest userRequest, Map<String, Object> attributes, OAuth2UserInfo userInfo) {
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        if (userNameAttributeName == null ||userNameAttributeName.isBlank()) {
            userNameAttributeName = "sub";
        }

        Member member = memberService.getOrSave(userInfo);
        return new PrincipalDetails(member, attributes, userNameAttributeName);
    }

    private String additionalParameter(OAuth2UserRequest userRequest, String key) {
        Object value = userRequest.getAdditionalParameters().get(key);
        return value instanceof String text ? text : null;
    }

    private Map<String, Object> decodeJwtPayload(String jwtToken) {
        try {
            String[] parts = jwtToken.split(JWT_DELIMITER_REGEX);
            if (parts.length < 2) {
                throw new AuthException(AuthErrorCode.APPLE_VALIDATE_JWT_ERROR);
            }
            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]), java.nio.charset.StandardCharsets.UTF_8);
            Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);
            if (claims.containsKey("sub")) {
                Object subObj = claims.get("sub");
                try {
                    claims.put("id", Math.abs(subObj.hashCode()));
                } catch (Exception e) {
                    throw new AuthException(AuthErrorCode.APPLE_JWT_DECODE_FAIL);
                }
            }
            return claims;
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.APPLE_TOKEN_PARSE_ERROR);
        }
    }
}
