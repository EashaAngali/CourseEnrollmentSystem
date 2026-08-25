package com.example.courseenrollmentsystem.DTO.StudentSemsterDto;

import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSemesterResponseDto {

    private Long studentSemesterId;

    private Long studentId;
    private String studentNumber;
    private String studentName;

    private Long programId;
    private String programName;

    private Long academicSemesterId;
    private String academicSemesterName;

    private Integer semesterNumber;

    private StudentSemesterStatus registrationStatus;

    private Double semesterGpa;

    private Double cumulativeGpa;
}