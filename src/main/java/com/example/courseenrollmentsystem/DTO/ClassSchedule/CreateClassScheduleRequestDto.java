package com.example.courseenrollmentsystem.DTO.ClassSchedule;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class CreateClassScheduleRequestDto {

    @NotNull(message = "Course offering id is required")
    private Long courseOfferingId;


    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;


    @NotNull(message = "Start time is required")
    private LocalTime startTime;


    @NotNull(message = "End time is required")
    private LocalTime endTime;


    private String room;
}