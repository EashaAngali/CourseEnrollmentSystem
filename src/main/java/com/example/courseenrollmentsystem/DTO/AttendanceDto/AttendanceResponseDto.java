package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceMethod;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AttendanceResponseDto {

    private Long attendanceId;

    private Long classSessionId;

    private Long enrollmentId;

    private Long studentId;
    private String studentNumber;
    private String studentName;

    private Long courseOfferingId;

    private Long courseId;
    private String courseCode;
    private String courseName;

    private LocalDate sessionDate;

    private AttendanceStatus status;

    private LocalDateTime markedAt;

    private AttendanceMethod attendanceMethod;
}
