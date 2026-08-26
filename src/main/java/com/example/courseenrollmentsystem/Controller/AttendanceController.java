package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.AttendanceDto.*;
import com.example.courseenrollmentsystem.Service.services.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {


    private final AttendanceService attendanceService;


    // =====================================================
    // QR ATTENDANCE
    // =====================================================

    @PostMapping("/qr")
    public ResponseEntity<AttendanceResponseDto>
    markAttendanceByQr(

            @Valid
            @RequestBody
            CreateAttendanceRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        attendanceService
                                .markAttendanceByQr(
                                        request
                                )
                );
    }


    // =====================================================
    // MANUAL ATTENDANCE
    // =====================================================

    @PostMapping("/manual")
    public ResponseEntity<AttendanceResponseDto>
    markManualAttendance(

            @Valid
            @RequestBody
            ManualAttendanceRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        attendanceService
                                .markManualAttendance(
                                        request
                                )
                );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponseDto>
    updateAttendance(

            @PathVariable
            Long attendanceId,

            @Valid
            @RequestBody
            UpdateAttendanceRequestDto request
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .updateAttendance(
                                attendanceId,
                                request
                        )
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponseDto>
    getAttendanceById(

            @PathVariable
            Long attendanceId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceById(
                                attendanceId
                        )
        );
    }


    // =====================================================
    // GET CLASS SESSION ATTENDANCE
    // =====================================================

    @GetMapping(
            "/class-session/{classSessionId}"
    )
    public ResponseEntity<List<AttendanceResponseDto>>
    getByClassSession(

            @PathVariable
            Long classSessionId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getByClassSession(
                                classSessionId
                        )
        );
    }


    // =====================================================
    // GET ENROLLMENT ATTENDANCE
    // =====================================================

    @GetMapping(
            "/enrollment/{enrollmentId}"
    )
    public ResponseEntity<List<AttendanceResponseDto>>
    getByEnrollment(

            @PathVariable
            Long enrollmentId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getByEnrollment(
                                enrollmentId
                        )
        );
    }


    // =====================================================
    // GET ENROLLMENT ATTENDANCE SUMMARY
    // =====================================================

    @GetMapping(
            "/enrollment/{enrollmentId}/summary"
    )
    public ResponseEntity<AttendanceSummaryResponseDto>
    getAttendanceSummary(

            @PathVariable
            Long enrollmentId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceSummary(
                                enrollmentId
                        )
        );
    }


    // =====================================================
    // GET STUDENT ATTENDANCE
    // =====================================================

    @GetMapping(
            "/student/{studentId}"
    )
    public ResponseEntity<List<AttendanceResponseDto>>
    getByStudent(

            @PathVariable
            Long studentId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getByStudent(
                                studentId
                        )
        );
    }


    // =====================================================
    // GET COURSE OFFERING ATTENDANCE
    // =====================================================

    @GetMapping(
            "/course-offering/{courseOfferingId}"
    )
    public ResponseEntity<List<AttendanceResponseDto>>
    getByCourseOffering(

            @PathVariable
            Long courseOfferingId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getByCourseOffering(
                                courseOfferingId
                        )
        );
    }
}