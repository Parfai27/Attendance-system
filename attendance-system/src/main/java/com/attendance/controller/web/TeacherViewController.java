package com.attendance.controller.web;

import com.attendance.dto.AttendanceMarkRequest;
import com.attendance.model.Attendance;
import com.attendance.model.ClassRoom;
import com.attendance.model.Teacher;
import com.attendance.service.AttendanceService;
import com.attendance.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/web/teachers")
public class TeacherViewController {

    private final TeacherService teacherService;
    private final AttendanceService attendanceService;

    public TeacherViewController(TeacherService teacherService,
                                 AttendanceService attendanceService) {
        this.teacherService = teacherService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/{teacherId}")
    public String dashboard(@PathVariable Long teacherId,
                            @RequestParam(required = false) Long classId,
                            @RequestParam(required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            Model model) {
        Teacher teacher = teacherService.getTeacher(teacherId);
        List<ClassRoom> classes = teacherService.getTeacherClasses(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("classes", classes);

        if (classId == null && !classes.isEmpty()) {
            classId = classes.get(0).getClassId();
        }
        model.addAttribute("activeClassId", classId);
        model.addAttribute("selectedDate", date);

        if (classId != null) {
            List<Attendance> attendanceRecords = teacherService.getClassAttendance(teacherId, classId, date);
            model.addAttribute("attendanceRecords", attendanceRecords);

            AttendanceMarkRequest form = new AttendanceMarkRequest();
            form.setClassId(classId);
            form.setDate(date != null ? date : LocalDate.now());
            model.addAttribute("attendanceForm", form);
        }

        return "teacher/dashboard";
    }

    @PostMapping("/{teacherId}/attendance")
    public String markAttendance(@PathVariable Long teacherId,
                                 @Valid @ModelAttribute("attendanceForm") AttendanceMarkRequest request,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please complete the attendance form.");
            return "redirect:/web/teachers/" + teacherId + "?classId=" + request.getClassId();
        }
        attendanceService.markAttendance(teacherId, request);
        redirectAttributes.addFlashAttribute("successMessage", "Attendance marked successfully.");
        return "redirect:/web/teachers/" + teacherId + "?classId=" + request.getClassId();
    }
}



