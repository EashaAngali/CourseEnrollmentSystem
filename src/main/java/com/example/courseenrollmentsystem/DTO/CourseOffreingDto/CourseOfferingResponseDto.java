package com.example.courseenrollmentsystem.DTO.CourseOffreingDto;

import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import lombok.*;


@Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class CourseOfferingResponseDto {

        private Long courseOfferingId;

        private Long programCourseId;

        private Long programId;
        private String programName;

        private Long courseId;
        private String courseCode;
        private String courseName;

        private Integer recommendedSemester;

        private Long academicSemesterId;
        private String academicSemesterName;

        private Long teacherId;
        private String teacherName;

        private Long sectionId;
        private String sectionCode;

        private Integer capacity;

        private CourseOfferingStatus status;
    }

