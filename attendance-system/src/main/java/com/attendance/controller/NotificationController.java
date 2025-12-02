package com.attendance.controller;

import com.attendance.dto.NotificationRequest;
import com.attendance.model.Notification;
import com.attendance.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasRole('ADMIN')")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.notifyUser(request.getUserId(), request.getMessage(), request.getChannel()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }
}

