package com.attendance.repository;

import com.attendance.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserUserId(Long userId);
    List<Notification> findTop5ByOrderByCreatedAtDesc();
}
