package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.ClassSchedule;
import com.example.courseenrollmentsystem.Enum.ClassSchedule.ClassScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

    import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

    public interface ClassScheduleRepository
            extends JpaRepository<ClassSchedule, Long> {


        List<ClassSchedule>
        findByCourseOffering_CourseOfferingId(
                Long courseOfferingId
        );


        List<ClassSchedule>
        findByCourseOffering_CourseOfferingIdAndStatus(
                Long courseOfferingId,
                ClassScheduleStatus status
        );


        boolean existsByCourseOffering_CourseOfferingIdAndDayOfWeekAndStartTime(
                Long courseOfferingId,
                DayOfWeek dayOfWeek,
                LocalTime startTime
        );
    }

