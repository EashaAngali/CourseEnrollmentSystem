package com.example.courseenrollmentsystem.Controller;
import com.example.courseenrollmentsystem.DTO.CourseOffreingDto.CourseOfferingResponseDto;
import com.example.courseenrollmentsystem.DTO.CourseOffreingDto.CreateCourseOfferingRequestDto;
import com.example.courseenrollmentsystem.DTO.CourseOffreingDto.UpdateCourseOfferingRequestDto;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import com.example.courseenrollmentsystem.Service.services.CourseOfferingService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/course-offerings")
@RequiredArgsConstructor
public class CourseOfferingController {

    private final CourseOfferingService courseOfferingService;


    // =====================================================
    // CREATE COURSE OFFERING
    // =====================================================

    @PostMapping
    public ResponseEntity<CourseOfferingResponseDto>
    createCourseOffering(

            @Valid
            @RequestBody CreateCourseOfferingRequestDto request
    ) {

        CourseOfferingResponseDto response =
                courseOfferingService
                        .createCourseOffering(request);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getAllCourseOfferings() {

        return ResponseEntity.ok(
                courseOfferingService
                        .getAllCourseOfferings()
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{courseOfferingId}")
    public ResponseEntity<CourseOfferingResponseDto>
    getCourseOfferingById(

            @PathVariable
            Long courseOfferingId
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getCourseOfferingById(
                                courseOfferingId
                        )
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{courseOfferingId}")
    public ResponseEntity<CourseOfferingResponseDto>
    updateCourseOffering(

            @PathVariable
            Long courseOfferingId,

            @Valid
            @RequestBody
            UpdateCourseOfferingRequestDto request
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .updateCourseOffering(
                                courseOfferingId,
                                request
                        )
        );
    }


    // =====================================================
    // CHANGE STATUS
    // =====================================================

    @PatchMapping("/{courseOfferingId}/status")
    public ResponseEntity<CourseOfferingResponseDto>
    changeStatus(

            @PathVariable
            Long courseOfferingId,

            @RequestParam
            CourseOfferingStatus status
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .changeStatus(
                                courseOfferingId,
                                status
                        )
        );
    }


    // =====================================================
    // GET BY ACADEMIC SEMESTER
    // =====================================================

    @GetMapping("/semester/{academicSemesterId}")
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getByAcademicSemester(

            @PathVariable
            Long academicSemesterId
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getByAcademicSemester(
                                academicSemesterId
                        )
        );
    }


    // =====================================================
    // GET BY TEACHER
    // =====================================================

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getByTeacher(

            @PathVariable
            Long teacherId
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getByTeacher(teacherId)
        );
    }


    // =====================================================
    // GET BY SECTION
    // =====================================================

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getBySection(

            @PathVariable
            Long sectionId
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getBySection(sectionId)
        );
    }


    // =====================================================
    // GET BY PROGRAM
    // =====================================================

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getByProgram(

            @PathVariable
            Long programId
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getByProgram(programId)
        );
    }


    // =====================================================
    // GET BY STATUS
    // =====================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getByStatus(

            @PathVariable
            CourseOfferingStatus status
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getByStatus(status)
        );
    }


    // =====================================================
    // GET BY SEMESTER + STATUS
    // Example:
    // /semester/1/status/OPEN
    // =====================================================

    @GetMapping(
            "/semester/{academicSemesterId}/status/{status}"
    )
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getBySemesterAndStatus(

            @PathVariable
            Long academicSemesterId,

            @PathVariable
            CourseOfferingStatus status
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getBySemesterAndStatus(
                                academicSemesterId,
                                status
                        )
        );
    }


    // =====================================================
    // GET TEACHER OFFERINGS BY SEMESTER
    //
    // Example:
    // /teacher/5/semester/2
    // =====================================================

    @GetMapping(
            "/teacher/{teacherId}/semester/{academicSemesterId}"
    )
    public ResponseEntity<List<CourseOfferingResponseDto>>
    getTeacherOfferingsBySemester(

            @PathVariable
            Long teacherId,

            @PathVariable
            Long academicSemesterId
    ) {

        return ResponseEntity.ok(
                courseOfferingService
                        .getTeacherOfferingsBySemester(
                                teacherId,
                                academicSemesterId
                        )
        );
    }
}