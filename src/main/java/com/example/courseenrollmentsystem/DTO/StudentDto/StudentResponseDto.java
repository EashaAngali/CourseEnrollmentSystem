package com.example.courseenrollmentsystem.DTO.StudentDto;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentGender;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentSpringOrFallStatus;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    private Long studentId;

    private String studentNumber;


    // Personal Information

    private String firstName;

    private String lastName;

    private String fullName;

    private String fatherName;

    private String email;

    private String phone;

    private String cnic;

    private LocalDate dateOfBirth;

    private StudentGender gender;

    private String address;


    // Academic Information

    private Long programId;

    private String programName;

    private String programCode;

    private LocalDate admissionDate;

    private StudentStatus status;
    private StudentSpringOrFallStatus springOrFallStatus;

}
