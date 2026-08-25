package com.example.courseenrollmentsystem.DTO.CourseDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CourseUpdateRequestDto {
    private String courseName;
    private String courseDescription;
    private String courseCategory;
    private String courseCode;
    private String duration;
    private String courseFee;
    private String courseStatus;
}
