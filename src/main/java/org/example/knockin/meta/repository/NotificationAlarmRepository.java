package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.NotificationAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationAlarmRepository extends JpaRepository<NotificationAlarm, Long> {
}