package com.example.courseenrollmentsystem.DTO.SectionDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SectionRequestDto {
    @NotBlank(message = "Section name is required")
    private String sectionName;

    @NotNull(message = "Program id is required")
    private Long programId;

    @NotNull(message = "Semester number is required")
    @Positive(message = "Semester number must be positive")
    private Integer semesterNumber;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;
}

