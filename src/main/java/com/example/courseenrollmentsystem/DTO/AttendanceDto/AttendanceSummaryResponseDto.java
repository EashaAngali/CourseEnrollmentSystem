package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummaryResponseDto {

    private Long studentId;

    private String studentNumber;

    private String studentName;

    private Long courseOfferingId;

    private String courseCode;

    private String courseName;

    private Long totalClasses;

    private Long presentCount;

    private Long absentCount;

    private Long lateCount;

    private Long excusedCount;

    private Double attendancePercentage;
}
