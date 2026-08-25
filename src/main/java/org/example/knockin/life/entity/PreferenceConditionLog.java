package org.example.knockin.life.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "preference_condition_log")
public class PreferenceConditionLog extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_pattern_information_id", nullable = false)
    private LifePatternInformation lifePatternInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_condition_log_degree_id")
    private PreferenceConditionLogDegree preferenceConditionLogDegree;
}
