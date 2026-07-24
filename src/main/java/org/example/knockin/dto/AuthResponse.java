package org.example.knockin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.*;
import org.example.knockin.entity.member.Gender;
import org.example.knockin.entity.member.MemberPrivacyType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Valid
    @NotNull(message = "인증 토큰이 누락되었습니다.")
    private String accessToken;

    private Long memberId;
    private String name;
    private LocalDate birth;
    private Integer memberAge;
    private Gender gender;
    private String profileImageUrl;
    private MemberPrivacyType visibility;
    // 기존 앱 필드는 유지하고, 이름이 분명한 완료 필드도 같이 내려준다.
    private boolean basicInfo;
    private boolean profileCompleted;
    private boolean preferenceInfo;
    private boolean preferenceCompleted;
    private DeleteInfo deleteInfo;

    @Data
    public static class DeleteInfo {
        private boolean isDelete;
        private String reason;
    }
}
