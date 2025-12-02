package com.attendance.controller.web;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/web/students")
public class StudentViewController {

    private final StudentService studentService;

    public StudentViewController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public String dashboard(@PathVariable Long studentId, Model model) {
        Student student = studentService.getStudent(studentId);
        List<Attendance> attendance = studentService.getAttendance(studentId);

        model.addAttribute("student", student);
        model.addAttribute("attendanceRecords", attendance);
        return "student/dashboard";
    }
}



