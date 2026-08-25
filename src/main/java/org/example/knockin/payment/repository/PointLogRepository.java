package org.example.knockin.payment.repository;

import org.example.knockin.payment.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointLogRepository  extends JpaRepository<PointLog, Long> {
}
