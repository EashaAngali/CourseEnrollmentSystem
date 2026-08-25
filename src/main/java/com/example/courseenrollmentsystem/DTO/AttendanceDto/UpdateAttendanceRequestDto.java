package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateAttendanceRequestDto {
    @NotNull
    private AttendanceStatus status;
}
