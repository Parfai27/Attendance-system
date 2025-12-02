package com.attendance.service;

import com.attendance.dto.AttendanceSummaryResponse;
import com.attendance.dto.ReportGenerationRequest;
import com.attendance.model.Attendance;
import com.attendance.model.AttendanceStatus;
import com.attendance.model.ClassRoom;
import com.attendance.model.Report;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.ClassRoomRepository;
import com.attendance.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ClassRoomRepository classRoomRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportService(ReportRepository reportRepository,
                         ClassRoomRepository classRoomRepository,
                         AttendanceRepository attendanceRepository) {
        this.reportRepository = reportRepository;
        this.classRoomRepository = classRoomRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public Report generateReport(ReportGenerationRequest request) {
        ClassRoom classRoom = classRoomRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        AttendanceSummaryResponse summary = summarizeClassAttendance(
                request.getClassId(),
                request.getFromDate(),
                request.getToDate()
        );

        Report report = new Report();
        report.setClassRoom(classRoom);
        report.setTeacher(classRoom.getTeacher());
        report.setGeneratedOn(LocalDate.now());
        report.setType(request.getType());
        report.setSummary(buildSummaryText(summary, request));
        return reportRepository.save(report);
    }

    public List<Report> listReports() {
        return reportRepository.findAll();
    }

    public AttendanceSummaryResponse summarizeClassAttendance(Long classId, LocalDate from, LocalDate to) {
        List<Attendance> records = attendanceRepository.findByClassRoomClassId(classId);
        if (from != null && to != null) {
            records = records.stream()
                    .filter(a -> !a.getDate().isBefore(from) && !a.getDate().isAfter(to))
                    .toList();
        }

        long total = records.size();
        long present = records.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long absent = records.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long late = records.stream().filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        double rate = total == 0 ? 0 : (double) present / total * 100;

        return new AttendanceSummaryResponse(classId, present, absent, late, rate);
    }

    private String buildSummaryText(AttendanceSummaryResponse summary, ReportGenerationRequest request) {
        String range = request.getFromDate() != null && request.getToDate() != null
                ? String.format("between %s and %s", request.getFromDate(), request.getToDate())
                : "for all recorded dates";
        return String.format(
                """
                Report Type: %s
                Class ID: %d
                Period: %s
                Present: %d
                Absent: %d
                Late: %d
                Attendance Rate: %.2f%%
                """,
                request.getType(),
                summary.classId(),
                range,
                summary.presentCount(),
                summary.absentCount(),
                summary.lateCount(),
                summary.attendanceRate()
        );
    }
}



