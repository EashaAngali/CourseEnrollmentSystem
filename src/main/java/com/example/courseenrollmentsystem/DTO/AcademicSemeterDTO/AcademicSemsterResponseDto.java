package com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO;

import com.example.courseenrollmentsystem.Enum.AcadmeicSemsterEnum.AcademicSemsterStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@RequiredArgsConstructor
public class AcademicSemsterResponseDto {
    private Long academicSemsterId;
    private String semesterName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate enrollmentStartDate;
    private LocalDate enrollmentEndDate;
    private LocalDate dropDeadline;
    private AcademicSemsterStatus academicSemsterStatus;
}
