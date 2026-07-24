package org.example.knockin.entity.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.knockin.entity.member.Member;
import org.example.knockin.global.entity.CreatedAtEntity;

import java.time.LocalDateTime;


@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "room_profile")
public class RoomProfile extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, insertable = false, updatable = false)
    private RoomProfileType type;

    @Column(name ="is_comeable_at_negotiable")
    private Boolean isComeableAtNegotiable;

    @Column(name = "comeable_at", nullable = false)
    private LocalDateTime comeableAt;

    protected void updateCommonInfo(Boolean isComeableAtNegotiable, LocalDateTime comeableAt) {
        this.isComeableAtNegotiable = isComeableAtNegotiable;
        this.comeableAt = comeableAt;
    }
}
