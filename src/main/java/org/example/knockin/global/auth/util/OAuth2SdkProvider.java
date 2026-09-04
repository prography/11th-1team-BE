package org.example.knockin.global.auth.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.knockin.global.auth.dto.AppleSdkRequest;
import org.example.knockin.global.auth.dto.KakaoSdkRequest;
import org.example.knockin.global.auth.dto.OAuth2SdkRequest;

@Getter
@AllArgsConstructor
public enum OAuth2SdkProvider {
    KAKAO("kakao", KakaoSdkRequest.class),
    APPLE("apple",AppleSdkRequest.class);

    private final String registrationId;
    private final Class<? extends OAuth2SdkRequest> dtoClass;

    public static OAuth2SdkProvider findByRegistrationId(String id) {
        return Arrays.stream(values())
                .filter(provider -> provider.registrationId.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 서비스입니다: " + id));
    }
}

