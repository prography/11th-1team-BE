package org.example.knockin.meta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Valid
    @NotNull(message = "인증 토큰이 누락되었습니다.")
    private String accessToken;

    private String name;
    private boolean basicInfo;
    private boolean preferenceInfo;
    private DeleteInfo deleteInfo;

    @Data
    public static class DeleteInfo {
        private boolean isDelete;
        private String reason;
    }
}

