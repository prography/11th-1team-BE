package org.example.knockin.member.repository.row;

import java.time.LocalDate;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.global.util.HasMemberId;

public record ChattingRoomBasicInfoRow(
        Long memberId,
        String name,
        LocalDate birth,
        Gender gender,
        String profileImageUrl
) implements HasMemberId {
}
