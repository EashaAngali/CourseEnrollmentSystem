package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.ClassSessionDto.ClassSessionResponseDto;
import com.example.courseenrollmentsystem.DTO.ClassSessionDto.CreateClassSessionRequestDto;
import com.example.courseenrollmentsystem.DTO.ClassSessionDto.UpdateClassSessionRequestDto;
import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import com.example.courseenrollmentsystem.Service.services.ClassSessionService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/class-sessions")
@RequiredArgsConstructor
public class ClassSessionController {


    private final ClassSessionService classSessionService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<ClassSessionResponseDto>
    createClassSession(

            @Valid
            @RequestBody
            CreateClassSessionRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        classSessionService
                                .createClassSession(
                                        request
                                )
                );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ClassSessionResponseDto>>
    getAllClassSessions() {

        return ResponseEntity.ok(
                classSessionService
                        .getAllClassSessions()
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{classSessionId}")
    public ResponseEntity<ClassSessionResponseDto>
    getClassSessionById(

            @PathVariable
            Long classSessionId
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .getClassSessionById(
                                classSessionId
                        )
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{classSessionId}")
    public ResponseEntity<ClassSessionResponseDto>
    updateClassSession(

            @PathVariable
            Long classSessionId,

            @Valid
            @RequestBody
            UpdateClassSessionRequestDto request
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .updateClassSession(
                                classSessionId,
                                request
                        )
        );
    }


    // =====================================================
    // START SESSION
    // =====================================================

    @PatchMapping("/{classSessionId}/start")
    public ResponseEntity<ClassSessionResponseDto>
    startClassSession(

            @PathVariable
            Long classSessionId
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .startClassSession(
                                classSessionId
                        )
        );
    }


    // =====================================================
    // COMPLETE SESSION
    // =====================================================

    @PatchMapping("/{classSessionId}/complete")
    public ResponseEntity<ClassSessionResponseDto>
    completeClassSession(

            @PathVariable
            Long classSessionId
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .completeClassSession(
                                classSessionId
                        )
        );
    }


    // =====================================================
    // CANCEL SESSION
    // =====================================================

    @PatchMapping("/{classSessionId}/cancel")
    public ResponseEntity<ClassSessionResponseDto>
    cancelClassSession(

            @PathVariable
            Long classSessionId
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .cancelClassSession(
                                classSessionId
                        )
        );
    }


    // =====================================================
    // GET BY COURSE OFFERING
    // =====================================================

    @GetMapping(
            "/course-offering/{courseOfferingId}"
    )
    public ResponseEntity<List<ClassSessionResponseDto>>
    getByCourseOffering(

            @PathVariable
            Long courseOfferingId
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .getByCourseOffering(
                                courseOfferingId
                        )
        );
    }


    // =====================================================
    // COURSE OFFERING + STATUS
    // =====================================================

    @GetMapping(
            "/course-offering/{courseOfferingId}/status/{status}"
    )
    public ResponseEntity<List<ClassSessionResponseDto>>
    getByCourseOfferingAndStatus(

            @PathVariable
            Long courseOfferingId,

            @PathVariable
            ClassSessionStatus status
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .getByCourseOfferingAndStatus(
                                courseOfferingId,
                                status
                        )
        );
    }


    // =====================================================
    // GET BY STATUS
    // =====================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClassSessionResponseDto>>
    getByStatus(

            @PathVariable
            ClassSessionStatus status
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .getByStatus(status)
        );
    }


    // =====================================================
    // GET BY DATE
    //
    // Example:
    // /api/class-sessions/date/2026-08-23
    // =====================================================

    @GetMapping("/date/{sessionDate}")
    public ResponseEntity<List<ClassSessionResponseDto>>
    getByDate(

            @PathVariable
            LocalDate sessionDate
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .getByDate(sessionDate)
        );
    }


    // =====================================================
    // GET TEACHER CLASS SESSIONS
    // =====================================================

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ClassSessionResponseDto>>
    getByTeacher(

            @PathVariable
            Long teacherId
    ) {

        return ResponseEntity.ok(
                classSessionService
                        .getByTeacher(
                                teacherId
                        )
        );
    }
}
