package org.example.knockin.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "state")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE) private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20)
    private MemberState states;

    @Column(length = 500)
    private String rejectReason;

    public void activeState() {
        this.states = MemberState.ACTIVE;
        this.rejectReason = "";
    }

    public void rejectState(String rejectReason) {
        this.states = MemberState.INACTIVE;
        this.rejectReason = rejectReason;
    }
}
