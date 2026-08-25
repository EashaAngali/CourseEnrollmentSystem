package com.example.courseenrollmentsystem.Entity;
import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "student_semesters",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_academic_semester",
                        columnNames = {
                                "student_id",
                                "academic_semester_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentSemesterId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    private Student student;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "academic_semester_id",
            nullable = false
    )
    private AcademicSemster academicSemester;


    @Column(nullable = false)
    private Integer semesterNumber;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentSemesterStatus registrationStatus;


    private Double semesterGpa;


    private Double cumulativeGpa;
}