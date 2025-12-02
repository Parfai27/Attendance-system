package com.attendance.controller.web;

import com.attendance.dto.ClassroomRequest;
import com.attendance.dto.ParentRegistrationRequest;
import com.attendance.dto.StudentRegistrationRequest;
import com.attendance.dto.TeacherRegistrationRequest;
import com.attendance.model.ClassRoom;
import com.attendance.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/admin")
public class AdminViewController {

    private final AdminService adminService;

    public AdminViewController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("teachers", adminService.listTeachers());
        model.addAttribute("students", adminService.listStudents());
        model.addAttribute("classes", adminService.listClasses());
        model.addAttribute("parents", adminService.listParents());

        model.addAttribute("teacherForm", model.getAttribute("teacherForm") != null
                ? model.getAttribute("teacherForm")
                : new TeacherRegistrationRequest());
        model.addAttribute("parentForm", model.getAttribute("parentForm") != null
                ? model.getAttribute("parentForm")
                : new ParentRegistrationRequest());
        model.addAttribute("studentForm", model.getAttribute("studentForm") != null
                ? model.getAttribute("studentForm")
                : new StudentRegistrationRequest());
        model.addAttribute("classForm", model.getAttribute("classForm") != null
                ? model.getAttribute("classForm")
                : new ClassroomRequest());

        return "admin/dashboard";
    }

    @PostMapping("/teachers")
    public String createTeacher(@Valid @ModelAttribute("teacherForm") TeacherRegistrationRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.teacherForm", bindingResult);
            redirectAttributes.addFlashAttribute("teacherForm", request);
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix the highlighted fields.");
            return "redirect:/web/admin";
        }
        adminService.registerTeacher(request);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher created successfully.");
        return "redirect:/web/admin";
    }

    @PostMapping("/parents")
    public String createParent(@Valid @ModelAttribute("parentForm") ParentRegistrationRequest request,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.parentForm", bindingResult);
            redirectAttributes.addFlashAttribute("parentForm", request);
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix the highlighted fields.");
            return "redirect:/web/admin";
        }
        adminService.registerParent(request);
        redirectAttributes.addFlashAttribute("successMessage", "Parent created successfully.");
        return "redirect:/web/admin";
    }

    @PostMapping("/students")
    public String createStudent(@Valid @ModelAttribute("studentForm") StudentRegistrationRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.studentForm", bindingResult);
            redirectAttributes.addFlashAttribute("studentForm", request);
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix the highlighted fields.");
            return "redirect:/web/admin";
        }
        adminService.registerStudent(request);
        redirectAttributes.addFlashAttribute("successMessage", "Student created successfully.");
        return "redirect:/web/admin";
    }

    @PostMapping("/classes")
    public String createClass(@Valid @ModelAttribute("classForm") ClassroomRequest request,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.classForm", bindingResult);
            redirectAttributes.addFlashAttribute("classForm", request);
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix the highlighted fields.");
            return "redirect:/web/admin";
        }
        ClassRoom classRoom = adminService.createClassroom(request);
        redirectAttributes.addFlashAttribute("successMessage",
                "Class " + classRoom.getClassName() + " created successfully.");
        return "redirect:/web/admin";
    }
}

