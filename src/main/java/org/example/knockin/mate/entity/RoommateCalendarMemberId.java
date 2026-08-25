package org.example.knockin.mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoommateCalendarMemberId implements Serializable {
    @Column(name = "roommate_calendar_id")
    Long roommateCalendarId;
    @Column(name = "member_id")
    Long memberId;
}
