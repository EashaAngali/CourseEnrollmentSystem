package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.ClassSchedule.ClassScheduleResponseDto;
import com.example.courseenrollmentsystem.DTO.ClassSchedule.CreateClassScheduleRequestDto;
import com.example.courseenrollmentsystem.DTO.ClassSchedule.UpdateClassScheduleRequestDto;
import com.example.courseenrollmentsystem.DTO.ClassSessionDto.ClassSessionResponseDto;
import com.example.courseenrollmentsystem.Service.services.ClassScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/faculty/class-schedules")
@RequiredArgsConstructor
public class ClassScheduleController {


    private final ClassScheduleService classScheduleService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<ClassScheduleResponseDto>
    createClassSchedule(

            @Valid
            @RequestBody
            CreateClassScheduleRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        classScheduleService
                                .createClassSchedule(request)
                );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ClassScheduleResponseDto>>
    getAllClassSchedules() {

        return ResponseEntity.ok(
                classScheduleService
                        .getAllClassSchedules()
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{classScheduleId}")
    public ResponseEntity<ClassScheduleResponseDto>
    getById(

            @PathVariable
            Long classScheduleId
    ) {

        return ResponseEntity.ok(
                classScheduleService
                        .getById(classScheduleId)
        );
    }


    // =====================================================
    // GET BY COURSE OFFERING
    // =====================================================

    @GetMapping(
            "/course-offering/{courseOfferingId}"
    )
    public ResponseEntity<List<ClassScheduleResponseDto>>
    getByCourseOffering(

            @PathVariable
            Long courseOfferingId
    ) {

        return ResponseEntity.ok(
                classScheduleService
                        .getByCourseOffering(
                                courseOfferingId
                        )
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{classScheduleId}")
    public ResponseEntity<ClassScheduleResponseDto>
    updateClassSchedule(

            @PathVariable
            Long classScheduleId,

            @Valid
            @RequestBody
            UpdateClassScheduleRequestDto request
    ) {

        return ResponseEntity.ok(
                classScheduleService
                        .updateClassSchedule(
                                classScheduleId,
                                request
                        )
        );
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @PatchMapping(
            "/{classScheduleId}/activate"
    )
    public ResponseEntity<ClassScheduleResponseDto>
    activateSchedule(

            @PathVariable
            Long classScheduleId
    ) {

        return ResponseEntity.ok(
                classScheduleService
                        .activateSchedule(
                                classScheduleId
                        )
        );
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @PatchMapping(
            "/{classScheduleId}/deactivate"
    )
    public ResponseEntity<ClassScheduleResponseDto>
    deactivateSchedule(

            @PathVariable
            Long classScheduleId
    ) {

        return ResponseEntity.ok(
                classScheduleService
                        .deactivateSchedule(
                                classScheduleId
                        )
        );
    }


    // =====================================================
    // GENERATE SEMESTER CLASS SESSIONS
    // =====================================================

    @PostMapping(
            "/{classScheduleId}/generate-sessions"
    )
    public ResponseEntity<List<ClassSessionResponseDto>>
    generateSessions(

            @PathVariable
            Long classScheduleId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        classScheduleService
                                .generateSessions(
                                        classScheduleId
                                )
                );
    }


    // =====================================================
    // GET GENERATED SESSIONS
    // =====================================================

    @GetMapping(
            "/{classScheduleId}/sessions"
    )
    public ResponseEntity<List<ClassSessionResponseDto>>
    getGeneratedSessions(

            @PathVariable
            Long classScheduleId
    ) {

        return ResponseEntity.ok(
                classScheduleService
                        .getGeneratedSessions(
                                classScheduleId
                        )
        );
    }
}
