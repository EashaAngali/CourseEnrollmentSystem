package com.example.courseenrollmentsystem.DTO.CourseDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseCreateRequestDto {
    private Long id;
    private String courseName;
    private String courseDescription;
    private String courseCode;
    private String courseCategory;
    private String duration;
    private String courseFee;
    private String courseStatus;
    private Long departmentID;
}
