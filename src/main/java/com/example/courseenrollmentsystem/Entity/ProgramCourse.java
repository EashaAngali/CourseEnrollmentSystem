package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.CourseType;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.ProgramCourseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

//programCourseId
//        program
//course
//        recommendedSemester
//courseType
//        creditHours
//status
@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class ProgramCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programCourseId;
    private Integer recommendedSemester;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    @Min(1)
    private Integer creditHours;
    @Enumerated(EnumType.STRING)
    private ProgramCourseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    private Course courses;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="program_id")
    private Program  programs;
}
