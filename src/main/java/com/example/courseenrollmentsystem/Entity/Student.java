package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentGender;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentSpringOrFallStatus;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_number",
                        columnNames = "student_number"
                ),

                @UniqueConstraint(
                        name = "uk_student_email",
                        columnNames = "email"
                ),

                @UniqueConstraint(
                        name = "uk_student_cnic",
                        columnNames = "cnic"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;


    // Permanent university student number
    // Example: BSCS-2026-001
    @Column(
            name = "student_number",
            nullable = false,
            unique = true
    )
    private String studentNumber;


    // ==========================
    // PERSONAL INFORMATION
    // ==========================

    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    private String fatherName;


    @Column(
            nullable = false,
            unique = true
    )
    private String email;


    private String phone;


    @Column(unique = true)
    private String cnic;


    private LocalDate dateOfBirth;


    @Enumerated(EnumType.STRING)
    private StudentGender gender;


    private String address;


    // ==========================
    // ACADEMIC INFORMATION
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "program_id",
            nullable = false
    )
    private Program program;


    private LocalDate admissionDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status;
    @Enumerated(EnumType.STRING)
    private StudentSpringOrFallStatus  springOrFallStatus;
}