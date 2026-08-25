package com.example.courseenrollmentsystem.DTO.StudentDto;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentGender;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

    @Getter
    @Setter
    public class StudentUpdateRequestDto {

        @NotBlank(message = "First name is required")
        private String firstName;


        @NotBlank(message = "Last name is required")
        private String lastName;


        private String fatherName;


        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;


        private String phone;


        private String cnic;


        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;


        private StudentGender gender;


        private String address;


        private StudentStatus status;
    }

