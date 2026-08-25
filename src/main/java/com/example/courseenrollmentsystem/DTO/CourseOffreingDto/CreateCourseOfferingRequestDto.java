package com.example.courseenrollmentsystem.DTO.CourseOffreingDto;

import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Setter;

    @Getter
    @Setter
    public class CreateCourseOfferingRequestDto {

        @NotNull(message = "Program course id is required")
        private Long programCourseId;

        @NotNull(message = "Academic semester id is required")
        private Long academicSemesterId;

        @NotNull(message = "Teacher id is required")
        private Long teacherId;

        @NotNull(message = "Section id is required")
        private Long sectionId;

        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be greater than zero")
        private Integer capacity;
    }
