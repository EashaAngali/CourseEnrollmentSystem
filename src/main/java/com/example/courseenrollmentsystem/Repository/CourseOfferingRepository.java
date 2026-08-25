package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.CourseOffering;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseOfferingRepository
        extends JpaRepository<CourseOffering, Long> {

    boolean existsByProgramCourse_ProgramCourseIdAndAcademicSemester_AcademicSemsterIdAndSection_SectionId(
            Long programCourseId,
            Long academicSemesterId,
            Long sectionId
    );


    List<CourseOffering>
    findByAcademicSemester_AcademicSemsterId(
            Long academicSemesterId
    );


    List<CourseOffering>
    findByTeacher_TeacherId(
            Long teacherId
    );


    List<CourseOffering>
    findBySection_SectionId(
            Long sectionId
    );


    List<CourseOffering>
    findByProgramCourse_Programs_programId(
            Long programId
    );


    List<CourseOffering>
    findByStatus(
            CourseOfferingStatus status
    );


    List<CourseOffering>
    findByAcademicSemester_AcademicSemsterIdAndStatus(
            Long academicSemesterId,
            CourseOfferingStatus status
    );


    List<CourseOffering>
    findByTeacher_TeacherIdAndAcademicSemester_AcademicSemsterId(
            Long teacherId,
            Long academicSemesterId
    );
}
