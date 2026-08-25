package org.example.knockin.life.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.knockin.member.entity.Member;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "preference_condition_weight_log")
public class PreferenceConditionWeightLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) private Member member;

    @ManyToOne
    @JoinColumn(name = "life_pattern_id")
    private LifePattern lifePattern;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_condition_weight_log_degree_id")
    private PreferenceConditionWeightLogDegree preferenceConditionWeightLogDegree;
}
