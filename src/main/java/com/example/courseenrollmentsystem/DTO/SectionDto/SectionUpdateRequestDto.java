package com.example.courseenrollmentsystem.DTO.SectionDto;

import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionUpdateRequestDto {

    @NotBlank(message = "Section name is required")
    private String sectionName;

    @NotNull(message = "Semester number is required")
    @Positive(message = "Semester number must be positive")
    private Integer semesterNumber;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    @NotNull(message = "Status is required")
    private SectionStatus status;
}