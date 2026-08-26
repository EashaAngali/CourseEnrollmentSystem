package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummaryResponseDto {

    private Long enrollmentId;

    private Long studentId;
    private String studentName;

    private Long courseOfferingId;

    private String courseCode;
    private String courseName;

    private long totalClasses;

    private long presentCount;

    private long absentCount;

    private long lateCount;

    private long excusedCount;

    private double attendancePercentage;
}