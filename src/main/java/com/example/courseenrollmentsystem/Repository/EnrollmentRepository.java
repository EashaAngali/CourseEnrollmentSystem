package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Enrollment;
import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentSemester_StudentSemesterIdAndCourseOffering_CourseOfferingId(
            Long studentSemesterId,
            Long courseOfferingId
    );

    List<Enrollment>
    findByStudentSemester_StudentSemesterId(
            Long studentSemesterId
    );

    List<Enrollment>
    findByStudentSemester_Student_StudentId(
            Long studentId
    );

    List<Enrollment>
    findByStudentSemester_Student_StudentIdAndStatus(
            Long studentId,
            EnrollmentStatus status
    );

    List<Enrollment>
    findByCourseOffering_CourseOfferingId(
            Long courseOfferingId
    );

    List<Enrollment>
    findByCourseOffering_CourseOfferingIdAndStatus(
            Long courseOfferingId,
            EnrollmentStatus status
    );

    long countByCourseOffering_CourseOfferingIdAndStatus(
            Long courseOfferingId,
            EnrollmentStatus status
    );
}