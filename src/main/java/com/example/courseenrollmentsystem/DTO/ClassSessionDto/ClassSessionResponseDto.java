package com.example.courseenrollmentsystem.DTO.ClassSessionDto;
import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSessionResponseDto {

    private Long classSessionId;

    private Long courseOfferingId;

    private Long courseId;
    private String courseCode;
    private String courseName;

    private Long academicSemesterId;
    private String academicSemesterName;

    private Long teacherId;
    private String teacherName;

    private Long sectionId;
    private String sectionCode;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String topic;

    private ClassSessionStatus status;
}
