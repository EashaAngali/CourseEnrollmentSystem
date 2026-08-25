package com.example.courseenrollmentsystem.DTO.AttendanceDto;

import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManualAttendanceRequestDto {
    @NotNull(message = "ClassSessionId required")
    private Long classSessionId;
    @NotNull(message = "please Add qrToken")
    private Long enrollmentId;
    @NotNull(message = "Status Required")
    private AttendanceStatus status;
}
