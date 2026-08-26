package com.example.courseenrollmentsystem.DTO.ClassSchedule;

import com.example.courseenrollmentsystem.Enum.ClassSchedule.ClassScheduleStatus;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleResponseDto {

    private Long classScheduleId;

    private Long courseOfferingId;

    private Long courseId;
    private String courseCode;
    private String courseName;

    private Long teacherId;
    private String teacherName;

    private Long sectionId;
    private String sectionCode;

    private Long academicSemesterId;
    private String academicSemesterName;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private String room;

    private ClassScheduleStatus status;
}