package com.example.courseenrollmentsystem.DTO.TeacherDto;

import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherDesignation;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@RequiredArgsConstructor
public class TeacherRequestDto {
    @NotBlank(message = "Please Enter your First name ")
    private String TeacherFirstName;
    @NotBlank(message = "Please Enter your Last name ")
    private String TeacherLastName;
    @Email(message = "Wrong email format")
    @NotBlank(message = "Please Enter your email ")
    private String teacherEmail;
    @NotNull(message = "Phone Number Required")
    @Pattern(regexp = "^(\\+92\\d{10})$"
            ,message = "Phone pattern must be +920*********")
    private String TeacherPhone;
    @NotBlank(message = "Please Enter Joining Date")
    private LocalDate joiningDate;
    @NotBlank(message = "Please Enter Designation")
    private TeacherDesignation teacherDesignation;
    @NotBlank(message = "Please Enter status")
    private TeacherStatus teacherStatus;
    private Long departmentId;
}
