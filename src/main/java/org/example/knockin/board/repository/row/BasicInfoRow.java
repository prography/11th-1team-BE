package org.example.knockin.board.repository.row;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.example.knockin.member.entity.Gender;

public record BasicInfoRow(
        Long boardId,
        String title,
        Integer deposit,
        Integer managementCost,
        Integer monthlyRent,
        String roomTypeName,
        String regionName,
        String parentRegionName,
        String grandParentRegionName,
        Boolean comeableDateNegotiable,
        LocalDateTime comeableDate,
        LocalDateTime createdAt,
        Long hits,
        String contents,
        Long memberId,
        String memberName,
        String memberProfileImageUrl,
        LocalDate birth,
        Gender gender
) {
}
