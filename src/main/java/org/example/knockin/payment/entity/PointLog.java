package org.example.knockin.payment.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.entity.CreatedAtEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_log")
public class PointLog extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long points;

    @Column(length = 50)
    private String reason;

    @Enumerated(EnumType.STRING)
    private VarianceType variance;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) private Member member;
}
