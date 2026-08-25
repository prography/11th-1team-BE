package org.example.knockin.mate.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.knockin.member.entity.Member;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoommateCalendarMember {
    @EmbeddedId
    private RoommateCalendarMemberId id;

    @MapsId("roommateCalendarId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_roommate_id", nullable = false)
    private RoommateCalendar roommateCalendar;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) private Member member;

    public static RoommateCalendarMember of(RoommateCalendar roommateCalendar, Member member) {
        RoommateCalendarMemberId id = RoommateCalendarMemberId.builder()
                .memberId(member.getId())
                .roommateCalendarId(roommateCalendar.getId())
                .build();

        return RoommateCalendarMember.builder()
                .id(id)
                .roommateCalendar(roommateCalendar)
                .member(member)
                .build();
    }

    public Long getMemberId() {
        return this.getId().getMemberId();
    }
}
