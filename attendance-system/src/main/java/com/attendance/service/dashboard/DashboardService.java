package com.attendance.service.dashboard;

import com.attendance.model.Attendance;
import com.attendance.model.Notification;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.ClassRoomRepository;
import com.attendance.repository.NotificationRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final AttendanceRepository attendanceRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(TeacherRepository teacherRepository,
                            StudentRepository studentRepository,
                            ClassRoomRepository classRoomRepository,
                            AttendanceRepository attendanceRepository,
                            NotificationRepository notificationRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.classRoomRepository = classRoomRepository;
        this.attendanceRepository = attendanceRepository;
        this.notificationRepository = notificationRepository;
    }

    public AdminSnapshot adminSnapshot() {
        long teachers = teacherRepository.count();
        long students = studentRepository.count();
        long classes = classRoomRepository.count();
        long attendanceRecords = attendanceRepository.count();

        List<Attendance> recentAttendance = attendanceRepository.findTop10ByOrderByDateDesc();
        List<Notification> recentNotifications = notificationRepository.findTop5ByOrderByCreatedAtDesc();

        double presentRate = attendanceRecords == 0 ? 0 :
                (double) recentAttendance.stream().filter(a -> a.getStatus().name().equals("PRESENT")).count()
                        / recentAttendance.size() * 100;

        return new AdminSnapshot(teachers, students, classes, attendanceRecords, presentRate,
                recentAttendance, recentNotifications);
    }

    public record AdminSnapshot(long teacherCount,
                                long studentCount,
                                long classCount,
                                long attendanceCount,
                                double presentRate,
                                List<Attendance> recentAttendance,
                                List<Notification> recentNotifications) {
    }
}

