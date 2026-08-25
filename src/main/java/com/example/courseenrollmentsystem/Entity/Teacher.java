package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherDesignation;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long teacherId;
    @Column(name="employee_code",unique = true,nullable = false)
    private String employeeCode;
    @NotBlank(message = "Please add First Name")
    private String TeacherFirstName;
    @NotBlank(message = "Please add Last Name")
    private String TeacherLastName;
    @Column(nullable = false)
    @Email(message = "Wrong Email format")
    private String teacherEmail;
    @Column(nullable = false)
    @NotNull(message = "Phone Number Required")
    @Pattern(regexp = "^(\\+92\\d{10})$"
            ,message = "Phone pattern must be +920*********")
    private String TeacherPhone;
    @Column(nullable = false)
    private LocalDate joiningDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeacherDesignation teacherDesignation;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeacherStatus teacherStatus = TeacherStatus.INACTIVE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
