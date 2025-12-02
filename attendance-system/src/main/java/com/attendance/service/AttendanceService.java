package com.attendance.service;

import com.attendance.dto.AttendanceMarkRequest;
import com.attendance.model.Attendance;
import com.attendance.model.AttendanceStatus;
import com.attendance.model.ClassRoom;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.ClassRoomRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final TeacherRepository teacherRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             ClassRoomRepository classRoomRepository,
                             TeacherRepository teacherRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.classRoomRepository = classRoomRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public Attendance markAttendance(Long teacherId, AttendanceMarkRequest request) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        ClassRoom classRoom = classRoomRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        if (!classRoom.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {
            throw new IllegalArgumentException("Teacher not assigned to this class");
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setClassRoom(classRoom);
        attendance.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        attendance.setStatus(request.getStatus());
        attendance.setMarkedBy(teacher);
        attendance.setRemarks(request.getRemarks());

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getClassAttendance(Long classId, LocalDate date) {
        if (date != null) {
            return attendanceRepository.findByClassRoomClassIdAndDate(classId, date);
        }
        return attendanceRepository.findByClassRoomClassId(classId);
    }

    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentStudentId(studentId);
    }

    public List<Attendance> getStudentAttendanceBetween(Long studentId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return getStudentAttendance(studentId);
        }
        return attendanceRepository.findByStudentStudentIdAndDateBetween(studentId, from, to);
    }

    public long countByStatus(List<Attendance> records, AttendanceStatus status) {
        return records.stream().filter(a -> a.getStatus() == status).count();
    }
}



