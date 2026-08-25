package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.StudentDto.StudentCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentResponseDto;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Student;

import java.util.List;
import java.util.Map;

public interface Studentinterface {


    StudentResponseDto addstudent(StudentCreateRequestDto studentCreateRequestDto);

    List<StudentResponseDto> getAllStudent();

    StudentResponseDto getStudentbyId(Long id);

    String deleteStudent(Long id);

    StudentResponseDto updateStudent(Long id, StudentUpdateRequestDto studentupdateRequestDto);


    StudentResponseDto updateStudentByCatagory(Long id, Map<String, Object> map);
}
