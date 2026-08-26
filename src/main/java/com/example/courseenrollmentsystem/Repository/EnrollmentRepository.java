package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Enrollment;
import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentSemester_StudentSemesterIdAndCourseOffering_CourseOfferingId(
            Long studentSemesterId,
            Long courseOfferingId
    );
   Optional<Enrollment> findByStudentSemester_Student_StudentCard_QrToken(String qrToken);



   List<Enrollment>
    findByStudentSemester_StudentSemesterId(
            Long studentSemesterId
    );
   boolean existsByStudentSemester_Student_StudentIdAndCourseOffering_CourseOfferingId(
           Long  studentId,
           Long courseOfferingId);
    List<Enrollment>
    findByStudentSemester_Student_StudentId(
            Long studentId
    );

    List<Enrollment>
    findByStudentSemester_Student_StudentIdAndStatus(
            Long studentId,
            EnrollmentStatus status
    );
    Optional<Enrollment>
    findByStudentSemester_Student_StudentIdAndCourseOffering_CourseOfferingIdAndStatus(
            Long studentId,
            Long courseOfferingId,
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