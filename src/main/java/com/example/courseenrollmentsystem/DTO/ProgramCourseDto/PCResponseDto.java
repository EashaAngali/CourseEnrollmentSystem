package com.example.courseenrollmentsystem.DTO.ProgramCourseDto;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.CourseType;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.ProgramCourseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PCResponseDto {
    private Long programCourseId;
    private Long programId;
    private String programName;
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer recommendedSemester;
    private CourseType courseType;
    private Integer creditHours;
    private ProgramCourseStatus status;
}
