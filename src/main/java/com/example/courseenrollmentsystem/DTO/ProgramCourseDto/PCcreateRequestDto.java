package com.example.courseenrollmentsystem.DTO.ProgramCourseDto;

import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.CourseType;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.ProgramCourseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PCcreateRequestDto {
    private Long programId;
    private Long id;
    private Integer recommendedSemester;
    private CourseType courseType;
    private Integer creditHours;
    private ProgramCourseStatus status;
}
