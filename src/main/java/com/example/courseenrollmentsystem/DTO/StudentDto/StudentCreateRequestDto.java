package com.example.courseenrollmentsystem.DTO.StudentDto;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentGender;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

    @Getter
    @Setter
    public class StudentCreateRequestDto {

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


        @NotNull(message = "Program id is required")
        private Long programId;


        private LocalDate admissionDate;
        private StudentStatus status;
    }

