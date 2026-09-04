package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.knockin.mate.entity.RepeatType;

public class CalendarEditDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "반복 타입")
        private List<RepeatType> repeatType;

        @Schema(description = "담당자 목록")
        private List<MemberInfo> members;

        @Schema(description = "카테고리명 목록")
        private List<String> categoryNames;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class MemberInfo {
        @Schema(description = "회원 고유 식별 ID")
        Long memberId;

        @Schema(description = "이름")
        String name;

        @Schema(description = "본인 여부")
        Boolean isMe;
    }
}
