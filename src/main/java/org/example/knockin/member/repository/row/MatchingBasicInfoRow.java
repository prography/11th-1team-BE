package org.example.knockin.member.repository.row;

import java.time.LocalDate;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.room.entity.RoomProfileType;
import org.example.knockin.global.util.HasMemberId;

public record MatchingBasicInfoRow(
        Long memberId,
        String memberProfileImageUrl,
        String memberName,
        LocalDate birth,
        Gender gender,
        Long roomProfileId,
        RoomProfileType roomProfileType
) implements HasMemberId {
}
