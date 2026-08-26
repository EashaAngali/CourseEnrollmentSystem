package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAttendanceRequestDto {
    @NotNull(message = "ClassSessionId required")
    private Long classSessionId;
    @NotNull(message = "please Add qrToken")
    private String qrToken;
}
