package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.ClassSession;
import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClassSessionRepository
        extends JpaRepository<ClassSession, Long> {


    List<ClassSession>
    findByCourseOffering_CourseOfferingId(
            Long courseOfferingId
    );


    List<ClassSession>
    findByCourseOffering_CourseOfferingIdAndStatus(
            Long courseOfferingId,
            ClassSessionStatus status
    );


    List<ClassSession>
    findByStatus(
            ClassSessionStatus status
    );


    List<ClassSession>
    findBySessionDate(
            LocalDate sessionDate
    );


    List<ClassSession>
    findByCourseOffering_Teacher_TeacherId(
            Long teacherId
    );


    boolean existsByCourseOffering_CourseOfferingIdAndStatus(
            Long courseOfferingId,
            ClassSessionStatus status
    );
    boolean existsByClassSchedule_ClassScheduleIdAndSessionDate(
            Long classScheduleId,
            LocalDate sessionDate
    );


    boolean existsByClassSchedule_ClassScheduleId(
            Long classScheduleId
    );


    List<ClassSession>
    findByClassSchedule_ClassScheduleId(
            Long classScheduleId
    );
}
