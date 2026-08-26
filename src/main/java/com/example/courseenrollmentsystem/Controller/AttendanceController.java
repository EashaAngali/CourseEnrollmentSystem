package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.AttendanceDto.AttendanceResponseDto;
import com.example.courseenrollmentsystem.DTO.AttendanceDto.CreateAttendanceRequestDto;
import com.example.courseenrollmentsystem.Service.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponseDto> MarkAttendance(CreateAttendanceRequestDto createAttendanceRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.markAttendance(createAttendanceRequestDto));
    }
}
