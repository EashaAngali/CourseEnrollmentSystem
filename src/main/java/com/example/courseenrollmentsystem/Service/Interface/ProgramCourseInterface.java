package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCResponseDto;
import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCcreateRequestDto;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.CourseType;

import java.util.List;
import java.util.Map;

public interface ProgramCourseInterface {
    PCResponseDto addprogramcourse(PCcreateRequestDto pccreateRequestDto);

    List<PCResponseDto> getAllProgramCourse();

    PCResponseDto getProgramCourseById(Long id);

    List<PCResponseDto> getCourseByProgramId(Long programId);

    List<PCResponseDto> getCourseByProgramIdAndSemster(Long programId,Integer semesterNumber);

    List<PCResponseDto> getCourseByCourseType(Long programId, CourseType courseType);

    PCResponseDto updateprogramcourse(Long id, Map<String, Object> updateRequestDto);
}
