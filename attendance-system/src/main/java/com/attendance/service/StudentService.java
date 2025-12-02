package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final AttendanceService attendanceService;

    public StudentService(StudentRepository studentRepository,
                          AttendanceService attendanceService) {
        this.studentRepository = studentRepository;
        this.attendanceService = attendanceService;
    }

    public Student getStudent(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }

    public List<Attendance> getAttendance(Long studentId) {
        getStudent(studentId);
        return attendanceService.getStudentAttendance(studentId);
    }

    public Student getStudentByUsername(String username) {
        return studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }
}

