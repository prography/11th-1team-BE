package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    Notification findByIdAndIsDeleted(Long id, Boolean isDeleted);
}