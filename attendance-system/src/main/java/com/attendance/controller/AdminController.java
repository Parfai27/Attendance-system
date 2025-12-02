package com.attendance.controller;

import com.attendance.dto.*;
import com.attendance.model.ClassRoom;
import com.attendance.model.Parent;
import com.attendance.model.Report;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.service.AdminService;
import com.attendance.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;

    public AdminController(AdminService adminService,
                           ReportService reportService) {
        this.adminService = adminService;
        this.reportService = reportService;
    }

    @PostMapping("/teachers")
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        return ResponseEntity.ok(adminService.registerTeacher(request));
    }

    @PostMapping("/parents")
    public ResponseEntity<Parent> createParent(@Valid @RequestBody ParentRegistrationRequest request) {
        return ResponseEntity.ok(adminService.registerParent(request));
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        return ResponseEntity.ok(adminService.registerStudent(request));
    }

    @PostMapping("/classes")
    public ResponseEntity<ClassRoom> createClass(@Valid @RequestBody ClassroomRequest request) {
        return ResponseEntity.ok(adminService.createClassroom(request));
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> listTeachers() {
        return ResponseEntity.ok(adminService.listTeachers());
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> listStudents() {
        return ResponseEntity.ok(adminService.listStudents());
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassRoom>> listClasses() {
        return ResponseEntity.ok(adminService.listClasses());
    }

    @PostMapping("/reports")
    public ResponseEntity<Report> generateReport(@Valid @RequestBody ReportGenerationRequest request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> listReports() {
        return ResponseEntity.ok(reportService.listReports());
    }
}

