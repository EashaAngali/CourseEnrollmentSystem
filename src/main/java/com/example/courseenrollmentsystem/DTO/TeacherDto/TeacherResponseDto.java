package com.example.courseenrollmentsystem.DTO.TeacherDto;

import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherDesignation;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@RequiredArgsConstructor
public class TeacherResponseDto {
    private Long Teacher_id;
    private String employeeCode;
    private String TeacherFirstName;
    private String TeacherLastName;
    private String teacherEmail;
    private String TeacherPhone;
    private LocalDate joiningDate;
    private TeacherDesignation teacherDesignation;
    private TeacherStatus teacherStatus;
}
