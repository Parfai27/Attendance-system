package com.attendance.controller.web;

import com.attendance.model.Attendance;
import com.attendance.model.Notification;
import com.attendance.model.Parent;
import com.attendance.model.Student;
import com.attendance.service.ParentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/web/parents")
public class ParentViewController {

    private final ParentService parentService;

    public ParentViewController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/{parentId}")
    public String dashboard(@PathVariable Long parentId,
                            @RequestParam(required = false) Long studentId,
                            Model model) {
        Parent parent = parentService.getParent(parentId);
        List<Student> children = parentService.getChildren(parentId);
        List<Notification> notifications = parentService.getNotifications(parentId);

        model.addAttribute("parent", parent);
        model.addAttribute("children", children);
        model.addAttribute("notifications", notifications);

        if (studentId == null && !children.isEmpty()) {
            studentId = children.get(0).getStudentId();
        }
        model.addAttribute("activeStudentId", studentId);

        if (studentId != null) {
            List<Attendance> records = parentService.getChildAttendance(parentId, studentId);
            model.addAttribute("attendanceRecords", records);
        }

        return "parent/dashboard";
    }
}



