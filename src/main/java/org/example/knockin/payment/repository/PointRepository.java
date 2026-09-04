package org.example.knockin.payment.repository;

import org.example.knockin.payment.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository  extends JpaRepository<Point, Long> {
}
