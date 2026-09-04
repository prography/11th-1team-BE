package org.example.knockin.chat.repository.row;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.example.knockin.chat.entity.ChattingRequiredStatus;
import org.example.knockin.member.entity.Gender;

public record ChatRequestListRow(
        Long requiredId,
        ChattingRequiredStatus status,
        Long memberId,
        String memberName,
        LocalDate birth,
        Gender gender,
        LocalDateTime createdAt
) {
}
