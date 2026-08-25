package org.example.knockin.chat.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.knockin.board.entity.RoommateBoard;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.entity.CreatedAtEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatting_required")
public class ChattingRequired extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestee", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member requestee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roommate_board_id")
    private RoommateBoard roommateBoard;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChattingRequiredStatus status;

    public boolean isPending() {
        return this.status == ChattingRequiredStatus.PENDING;
    }

    public void accept() {
        this.status = ChattingRequiredStatus.ACCEPTED;
    }

    public void reject() {
        this.status = ChattingRequiredStatus.REJECTED;
    }

    public void cancel() {
        this.status = ChattingRequiredStatus.CANCELED;
    }
}
