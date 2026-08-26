package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAttendanceRequestDto {
    @NotNull
    private AttendanceStatus status;
}
