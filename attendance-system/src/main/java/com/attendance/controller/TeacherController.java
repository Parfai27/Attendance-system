package com.attendance.controller;

import com.attendance.dto.AttendanceMarkRequest;
import com.attendance.model.Attendance;
import com.attendance.model.ClassRoom;
import com.attendance.service.AttendanceService;
import com.attendance.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/teachers")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    private final TeacherService teacherService;
    private final AttendanceService attendanceService;

    public TeacherController(TeacherService teacherService,
                             AttendanceService attendanceService) {
        this.teacherService = teacherService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/{teacherId}/classes")
    public ResponseEntity<List<ClassRoom>> getClasses(@PathVariable Long teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherClasses(teacherId));
    }

    @GetMapping("/{teacherId}/classes/{classId}/attendance")
    public ResponseEntity<List<Attendance>> viewClassAttendance(@PathVariable Long teacherId,
                                                                @PathVariable Long classId,
                                                                @RequestParam(required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                LocalDate date) {
        return ResponseEntity.ok(teacherService.getClassAttendance(teacherId, classId, date));
    }

    @PostMapping("/{teacherId}/attendance")
    public ResponseEntity<Attendance> markAttendance(@PathVariable Long teacherId,
                                                     @Valid @RequestBody AttendanceMarkRequest request) {
        return ResponseEntity.ok(attendanceService.markAttendance(teacherId, request));
    }
}

