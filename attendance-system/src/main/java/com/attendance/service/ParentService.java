package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.model.Notification;
import com.attendance.model.Parent;
import com.attendance.model.Student;
import com.attendance.repository.ParentRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final AttendanceService attendanceService;
    private final NotificationService notificationService;

    public ParentService(ParentRepository parentRepository,
                         StudentRepository studentRepository,
                         AttendanceService attendanceService,
                         NotificationService notificationService) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.attendanceService = attendanceService;
        this.notificationService = notificationService;
    }

    public Parent getParent(Long parentId) {
        return parentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
    }

    public List<Student> getChildren(Long parentId) {
        getParent(parentId);
        return studentRepository.findByParentParentId(parentId);
    }

    public List<Attendance> getChildAttendance(Long parentId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        if (student.getParent() == null || !student.getParent().getParentId().equals(parentId)) {
            throw new IllegalArgumentException("Student does not belong to parent");
        }
        return attendanceService.getStudentAttendance(studentId);
    }

    public List<Notification> getNotifications(Long parentId) {
        Parent parent = getParent(parentId);
        return notificationService.getNotificationsForUser(parent.getUser().getUserId());
    }

    public Parent getParentByUsername(String username) {
        return parentRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
    }
}

