package com.attendance.service;

import com.attendance.model.Notification;
import com.attendance.model.User;
import com.attendance.repository.NotificationRepository;
import com.attendance.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Notification notifyUser(Long userId, String message, String channel) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setChannel(channel);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserUserId(userId);
    }
}



