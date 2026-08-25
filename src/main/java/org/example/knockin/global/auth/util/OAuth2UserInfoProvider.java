package org.example.knockin.global.auth.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.knockin.global.auth.dto.AppleUserInfo;
import org.example.knockin.global.auth.dto.KakaoUserInfo;
import org.example.knockin.global.auth.dto.OAuth2UserInfo;

@Getter
@AllArgsConstructor
public enum OAuth2UserInfoProvider {
    KAKAO("kakao", KakaoUserInfo.class),
    APPLE("apple", AppleUserInfo.class);

    private final String registrationId;
    private final Class<? extends OAuth2UserInfo> infoClass;

    public static OAuth2UserInfoProvider findByRegistrationId(String id) {
        return Arrays.stream(values())
                .filter(provider -> provider.registrationId.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 서비스입니다: " + id));
    }
}
