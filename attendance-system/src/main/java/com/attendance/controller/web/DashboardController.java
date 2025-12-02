package com.attendance.controller.web;

import com.attendance.model.Parent;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.service.ParentService;
import com.attendance.service.StudentService;
import com.attendance.service.TeacherService;
import com.attendance.service.dashboard.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ParentService parentService;
    private final DashboardService dashboardService;

    public DashboardController(TeacherService teacherService,
                               StudentService studentService,
                               ParentService parentService,
                               DashboardService dashboardService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.parentService = parentService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/dashboard/admin";
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/dashboard/teacher";
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            return "redirect:/dashboard/student";
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PARENT"))) {
            return "redirect:/dashboard/parent";
        }
        return "redirect:/";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("snapshot", dashboardService.adminSnapshot());
        return "dashboard/admin";
    }

    @GetMapping("/dashboard/teacher")
    public String teacherDashboard(Authentication authentication) {
        Teacher teacher = teacherService.getTeacherByUsername(authentication.getName());
        return "redirect:/web/teachers/" + teacher.getTeacherId();
    }

    @GetMapping("/dashboard/student")
    public String studentDashboard(Authentication authentication) {
        Student student = studentService.getStudentByUsername(authentication.getName());
        return "redirect:/web/students/" + student.getStudentId();
    }

    @GetMapping("/dashboard/parent")
    public String parentDashboard(Authentication authentication) {
        Parent parent = parentService.getParentByUsername(authentication.getName());
        return "redirect:/web/parents/" + parent.getParentId();
    }
}

