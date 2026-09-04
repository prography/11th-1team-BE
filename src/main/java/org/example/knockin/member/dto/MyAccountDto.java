package org.example.knockin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.member.entity.MemberRole;

@Data
public class MyAccountDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "회원 권한 (USER, ADMIN 등)")
        private MemberRole role;
    }
}
