package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Attendance;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {


    boolean existsByClassSession_ClassSessionIdAndEnrollment_EnrollmentId(
            Long classSessionId,
            Long enrollmentId
    );


    Optional<Attendance>
    findByClassSession_ClassSessionIdAndEnrollment_EnrollmentId(
            Long classSessionId,
            Long enrollmentId
    );


    List<Attendance>
    findByClassSession_ClassSessionId(
            Long classSessionId
    );


    List<Attendance>
    findByEnrollment_EnrollmentId(
            Long enrollmentId
    );

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    Optional<Attendance> findByAttendanceDateAndAttendanceStatus(LocalDate date, AttendanceStatus status);


    List<Attendance>
    findByClassSession_CourseOffering_CourseOfferingId(
            Long courseOfferingId
    );


    long countByEnrollment_EnrollmentId(
            Long enrollmentId
    );


    long countByEnrollment_EnrollmentIdAndAttendanceStatus(
            Long enrollmentId,
            AttendanceStatus status
    );
}
