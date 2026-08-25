package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.CourseDto.CourseCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.CourseDto.CourseUpdateRequestDto;

import java.util.List;
import java.util.Map;

public interface CourseInterface {
    List<CourseCreateRequestDto> viewAllCourse();

    

    CourseCreateRequestDto addCourse(CourseCreateRequestDto createRequestDto);

    CourseCreateRequestDto getCoursebyId(Long id);

    String deleteCourse(Long id);


    CourseUpdateRequestDto updateCourse(Long id, CourseUpdateRequestDto courseUpdateRequestDto);

    CourseUpdateRequestDto updateCourseByCatagory(Long id, Map<String, Object> map);
}
