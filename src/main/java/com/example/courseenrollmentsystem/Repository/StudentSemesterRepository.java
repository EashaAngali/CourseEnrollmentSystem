package com.example.courseenrollmentsystem.Repository;
import com.example.courseenrollmentsystem.Entity.StudentSemester;
import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentSemesterRepository
        extends JpaRepository<StudentSemester, Long> {


    boolean existsByStudent_StudentIdAndAcademicSemester_AcademicSemsterId(
            Long studentId,
            Long academicSemesterId
    );


    List<StudentSemester> findByStudent_StudentId(
            Long studentId
    );


    List<StudentSemester>
    findByAcademicSemester_AcademicSemsterId(
            Long academicSemesterId
    );


    List<StudentSemester>
    findByStudent_StudentIdAndRegistrationStatus(
            Long studentId,
            StudentSemesterStatus registrationStatus
    );


    Optional<StudentSemester>
    findByStudent_StudentIdAndAcademicSemester_AcademicSemsterId(
            Long studentId,
            Long academicSemesterId
    );


    List<StudentSemester>
    findByRegistrationStatus(
            StudentSemesterStatus registrationStatus
    );
}