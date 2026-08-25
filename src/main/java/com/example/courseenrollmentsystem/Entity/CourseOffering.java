package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "course_offerings",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "program_course_id",
                                "academic_semester_id",
                                "section_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseOfferingId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "program_course_id",
            nullable = false
    )
    private ProgramCourse programCourse;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "academic_semester_id",
            nullable = false
    )
    private AcademicSemster academicSemester;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "teacher_id",
            nullable = false
    )
    private Teacher teacher;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "section_id",
            nullable = false
    )
    private Section section;


    @Column(nullable = false)
    private Integer capacity;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseOfferingStatus status;
}
