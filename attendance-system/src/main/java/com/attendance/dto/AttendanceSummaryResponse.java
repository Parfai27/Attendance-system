package com.attendance.dto;

public record AttendanceSummaryResponse(
        Long classId,
        long presentCount,
        long absentCount,
        long lateCount,
        double attendanceRate
) {
}



