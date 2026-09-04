package org.example.knockin.board.repository.row;

import java.time.LocalDateTime;

public record EditFormRow(
        String title,
        Integer deposit,
        Integer monthlyRent,
        Integer managementCost,
        Long roomTypeId,
        String roomTypeName,
        String roomTypeImageUrl,
        Long regionId,
        String regionName,
        String parentRegionName,
        String grandParentRegionName,
        Boolean comeableDateNegotiable,
        LocalDateTime comeableDate,
        String contents
) {
}
