package org.example.knockin.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.knockin.meta.entity.Alarm;
import org.example.knockin.meta.entity.AlarmType;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatting_required_alarm")
@DiscriminatorValue(AlarmType.Values.CHATTING_REQUIRED)
public class ChattingRequiredAlarm extends Alarm{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_required_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChattingRequired chattingRequired;
}
