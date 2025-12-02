package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.model.ClassRoom;
import com.attendance.model.Teacher;
import com.attendance.repository.ClassRoomRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassRoomRepository classRoomRepository;
    private final AttendanceService attendanceService;

    public TeacherService(TeacherRepository teacherRepository,
                          ClassRoomRepository classRoomRepository,
                          AttendanceService attendanceService) {
        this.teacherRepository = teacherRepository;
        this.classRoomRepository = classRoomRepository;
        this.attendanceService = attendanceService;
    }

    public Teacher getTeacher(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    }

    public Teacher getTeacherByUsername(String username) {
        return teacherRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    }

    public List<ClassRoom> getTeacherClasses(Long teacherId) {
        return classRoomRepository.findByTeacherTeacherId(teacherId);
    }

    public List<Attendance> getClassAttendance(Long teacherId, Long classId, LocalDate date) {
        Teacher teacher = getTeacher(teacherId);
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        if (!classRoom.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {
            throw new IllegalArgumentException("Teacher not assigned to this class");
        }
        return attendanceService.getClassAttendance(classId, date);
    }
}

