package org.example.knockin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppleSdkRequest implements OAuth2SdkRequest {
    @Valid
    @NotNull(message = "인증 정보가 누락되었습니다.")
    private AuthObj authObj;

    private String name;

    @Getter
    @NoArgsConstructor
    public static class AuthObj {
        @NotBlank(message = "액세스 토큰은 필수입니다.")
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;
    }

    public String getAccessToken() {
        return (authObj != null) ? authObj.getAccessToken() : null;
    }
}

