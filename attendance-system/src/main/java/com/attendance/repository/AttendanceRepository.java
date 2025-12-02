package com.attendance.repository;

import com.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByClassRoomClassIdAndDate(Long classId, LocalDate date);

    List<Attendance> findByClassRoomClassId(Long classId);

    List<Attendance> findByStudentStudentId(Long studentId);

    List<Attendance> findByStudentStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);

    List<Attendance> findTop10ByOrderByDateDesc();
}
