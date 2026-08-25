package com.example.courseenrollmentsystem.DTO.EnrollmentDto;

import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDto {

    private Long enrollmentId;

    private Long studentSemesterId;

    private Long studentId;
    private String studentNumber;
    private String studentName;

    private Integer semesterNumber;

    private Long courseOfferingId;

    private Long programId;
    private String programName;

    private Long courseId;
    private String courseCode;
    private String courseName;

    private Integer creditHours;

    private Long academicSemesterId;
    private String academicSemesterName;

    private Long teacherId;
    private String teacherName;

    private Long sectionId;
    private String sectionCode;

    private LocalDate enrollmentDate;
    private LocalDate dropDate;

    private EnrollmentStatus status;
}