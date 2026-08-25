package com.example.courseenrollmentsystem.Entity;
import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_semester_course_offering",
                        columnNames = {
                                "student_semester_id",
                                "course_offering_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_semester_id",
            nullable = false
    )
    private StudentSemester studentSemester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_offering_id",
            nullable = false
    )
    private CourseOffering courseOffering;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    private LocalDate dropDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;
}