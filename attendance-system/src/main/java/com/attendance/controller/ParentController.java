package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.model.Notification;
import com.attendance.model.Parent;
import com.attendance.model.Student;
import com.attendance.service.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/parents")
@PreAuthorize("hasRole('PARENT')")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<Parent> getParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(parentService.getParent(parentId));
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Student>> getChildren(@PathVariable Long parentId) {
        return ResponseEntity.ok(parentService.getChildren(parentId));
    }

    @GetMapping("/{parentId}/children/{studentId}/attendance")
    public ResponseEntity<List<Attendance>> getChildAttendance(@PathVariable Long parentId,
                                                               @PathVariable Long studentId) {
        return ResponseEntity.ok(parentService.getChildAttendance(parentId, studentId));
    }

    @GetMapping("/{parentId}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long parentId) {
        return ResponseEntity.ok(parentService.getNotifications(parentId));
    }
}

