package org.example.knockin.chat.repository;

import org.example.knockin.chat.entity.ChattingRequiredAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRequiredAlarmRepository extends JpaRepository<ChattingRequiredAlarm, Long> {
}