package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.AcadmeicSemsterEnum.AcademicSemsterStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

//academicSemesterId
//        semesterName
//startDate
//        endDate
//enrollmentStartDate
//        enrollmentEndDate
//dropDeadline
//        status
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class AcademicSemster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long academicSemsterId;
    private String semesterName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate enrollmentStartDate;
    private LocalDate enrollmentEndDate;
    private LocalDate dropDeadline;
    @Enumerated(EnumType.STRING)
    private AcademicSemsterStatus academicSemsterStatus;
}
