package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.StudentSemsterDto.CreateStudentSemesterRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentSemsterDto.StudentSemesterResponseDto;
import com.example.courseenrollmentsystem.DTO.StudentSemsterDto.UpdateStudentSemesterRequestDto;
import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import com.example.courseenrollmentsystem.Service.services.StudentSemesterService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/student-semesters")
@RequiredArgsConstructor
public class StudentSemesterController {


    private final StudentSemesterService studentSemesterService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<StudentSemesterResponseDto>
    createStudentSemester(

            @Valid
            @RequestBody
            CreateStudentSemesterRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        studentSemesterService
                                .createStudentSemester(request)
                );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<StudentSemesterResponseDto>>
    getAllStudentSemesters() {

        return ResponseEntity.ok(
                studentSemesterService
                        .getAllStudentSemesters()
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{studentSemesterId}")
    public ResponseEntity<StudentSemesterResponseDto>
    getStudentSemesterById(

            @PathVariable
            Long studentSemesterId
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .getStudentSemesterById(
                                studentSemesterId
                        )
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{studentSemesterId}")
    public ResponseEntity<StudentSemesterResponseDto>
    updateStudentSemester(

            @PathVariable
            Long studentSemesterId,

            @Valid
            @RequestBody
            UpdateStudentSemesterRequestDto request
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .updateStudentSemester(
                                studentSemesterId,
                                request
                        )
        );
    }


    // =====================================================
    // CHANGE REGISTRATION STATUS
    //
    // Example:
    // PATCH /api/student-semesters/5/status?status=REGISTERED
    // =====================================================

    @PatchMapping("/{studentSemesterId}/status")
    public ResponseEntity<StudentSemesterResponseDto>
    changeRegistrationStatus(

            @PathVariable
            Long studentSemesterId,

            @RequestParam
            StudentSemesterStatus status
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .changeRegistrationStatus(
                                studentSemesterId,
                                status
                        )
        );
    }


    // =====================================================
    // GET ALL SEMESTERS OF STUDENT
    // =====================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentSemesterResponseDto>>
    getByStudent(

            @PathVariable
            Long studentId
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .getByStudent(studentId)
        );
    }


    // =====================================================
    // GET ALL STUDENTS IN ACADEMIC SEMESTER
    // =====================================================

    @GetMapping("/academic-semester/{academicSemesterId}")
    public ResponseEntity<List<StudentSemesterResponseDto>>
    getByAcademicSemester(

            @PathVariable
            Long academicSemesterId
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .getByAcademicSemester(
                                academicSemesterId
                        )
        );
    }


    // =====================================================
    // GET SPECIFIC STUDENT + ACADEMIC SEMESTER
    // =====================================================

    @GetMapping(
            "/student/{studentId}/academic-semester/{academicSemesterId}"
    )
    public ResponseEntity<StudentSemesterResponseDto>
    getByStudentAndAcademicSemester(

            @PathVariable
            Long studentId,

            @PathVariable
            Long academicSemesterId
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .getByStudentAndAcademicSemester(
                                studentId,
                                academicSemesterId
                        )
        );
    }


    // =====================================================
    // GET BY REGISTRATION STATUS
    // =====================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<StudentSemesterResponseDto>>
    getByRegistrationStatus(

            @PathVariable
            StudentSemesterStatus status
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .getByRegistrationStatus(status)
        );
    }


    // =====================================================
    // GET STUDENT SEMESTERS BY STATUS
    // =====================================================

    @GetMapping(
            "/student/{studentId}/status/{status}"
    )
    public ResponseEntity<List<StudentSemesterResponseDto>>
    getStudentSemestersByStatus(

            @PathVariable
            Long studentId,

            @PathVariable
            StudentSemesterStatus status
    ) {

        return ResponseEntity.ok(
                studentSemesterService
                        .getStudentSemestersByStatus(
                                studentId,
                                status
                        )
        );
    }
}