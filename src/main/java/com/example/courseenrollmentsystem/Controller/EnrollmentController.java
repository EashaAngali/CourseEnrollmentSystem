package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.EnrollmentDto.CreateEnrollmentRequestDto;
import com.example.courseenrollmentsystem.DTO.EnrollmentDto.EnrollmentResponseDto;
import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import com.example.courseenrollmentsystem.Service.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/student/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<EnrollmentResponseDto>
    createEnrollment(

            @Valid
            @RequestBody
            CreateEnrollmentRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        enrollmentService
                                .createEnrollment(request)
                );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>>
    getAllEnrollments() {

        return ResponseEntity.ok(
                enrollmentService
                        .getAllEnrollments()
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentResponseDto>
    getEnrollmentById(

            @PathVariable
            Long enrollmentId
    ) {

        return ResponseEntity.ok(
                enrollmentService
                        .getEnrollmentById(
                                enrollmentId
                        )
        );
    }


    // =====================================================
    // DROP
    // =====================================================

    @PatchMapping("/{enrollmentId}/drop")
    public ResponseEntity<EnrollmentResponseDto>
    dropEnrollment(

            @PathVariable
            Long enrollmentId
    ) {

        return ResponseEntity.ok(
                enrollmentService
                        .dropEnrollment(
                                enrollmentId
                        )
        );
    }


    // =====================================================
    // GET BY STUDENT SEMESTER
    //
    // Current semester ke enrolled courses ke liye
    // =====================================================

    @GetMapping(
            "/student-semester/{studentSemesterId}"
    )
    public ResponseEntity<List<EnrollmentResponseDto>>
    getByStudentSemester(

            @PathVariable
            Long studentSemesterId
    ) {

        return ResponseEntity.ok(
                enrollmentService
                        .getByStudentSemester(
                                studentSemesterId
                        )
        );
    }


    // =====================================================
    // GET ALL ENROLLMENTS OF STUDENT
    //
    // Complete enrollment history
    // =====================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDto>>
    getByStudent(

            @PathVariable
            Long studentId
    ) {

        return ResponseEntity.ok(
                enrollmentService
                        .getByStudent(studentId)
        );
    }


    // =====================================================
    // STUDENT + STATUS
    // =====================================================

    @GetMapping(
            "/student/{studentId}/status/{status}"
    )
    public ResponseEntity<List<EnrollmentResponseDto>>
    getByStudentAndStatus(

            @PathVariable
            Long studentId,

            @PathVariable
            EnrollmentStatus status
    ) {

        return ResponseEntity.ok(
                enrollmentService
                        .getByStudentAndStatus(
                                studentId,
                                status
                        )
        );
    }


    // =====================================================
    // GET COURSE OFFERING STUDENTS
    // =====================================================

    @GetMapping(
            "/course-offering/{courseOfferingId}"
    )
    public ResponseEntity<List<EnrollmentResponseDto>>
    getByCourseOffering(

            @PathVariable
            Long courseOfferingId
    ) {

        return ResponseEntity.ok(
                enrollmentService
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
    public ResponseEntity<List<EnrollmentResponseDto>>
    getByCourseOfferingAndStatus(

            @PathVariable
            Long courseOfferingId,

            @PathVariable
            EnrollmentStatus status
    ) {

        return ResponseEntity.ok(
                enrollmentService
                        .getByCourseOfferingAndStatus(
                                courseOfferingId,
                                status
                        )
        );
    }
}
