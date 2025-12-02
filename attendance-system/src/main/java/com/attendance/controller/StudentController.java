package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/students")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudent(studentId));
    }

    @GetMapping("/{studentId}/attendance")
    public ResponseEntity<List<Attendance>> getAttendance(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getAttendance(studentId));
    }
}

