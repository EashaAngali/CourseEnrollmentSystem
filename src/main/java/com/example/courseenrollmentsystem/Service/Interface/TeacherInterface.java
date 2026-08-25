package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.TeacherDto.TeacherRequestDto;
import com.example.courseenrollmentsystem.DTO.TeacherDto.TeacherResponseDto;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface TeacherInterface {
    TeacherResponseDto saveTeacher(TeacherRequestDto teacherRequestDto);

    List<TeacherResponseDto> fetchAllTeachersData();

    TeacherResponseDto fetchTeacherById(Long id);

    TeacherResponseDto upadteTeacher(Long id, Map<String, Object> map);

    List<TeacherResponseDto> fetchTeacherBydepartmentId(Long id);

    List<TeacherResponseDto> fetchTeacherByStatus(TeacherStatus status);

    List<TeacherResponseDto> fetchTeacherByDepAndStatus(Long id, TeacherStatus status);

    List<TeacherResponseDto> fetchTeacherByName(String name);

    List<TeacherResponseDto> fetchTeacherByEmail(String email);

    TeacherResponseDto getTeacherByEmployeeCode(String id);

    List<TeacherResponseDto> page(PageRequest pageRequest);
}
