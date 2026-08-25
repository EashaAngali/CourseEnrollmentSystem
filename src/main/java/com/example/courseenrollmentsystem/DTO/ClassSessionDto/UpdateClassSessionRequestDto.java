package com.example.courseenrollmentsystem.DTO.ClassSessionDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UpdateClassSessionRequestDto {

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;


    @NotNull(message = "Start time is required")
    private LocalTime startTime;


    @NotNull(message = "End time is required")
    private LocalTime endTime;


    private String topic;
}
