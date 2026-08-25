package com.example.courseenrollmentsystem.Controller;
import com.example.courseenrollmentsystem.DTO.SectionDto.SectionRequestDto;
import com.example.courseenrollmentsystem.DTO.SectionDto.SectionResponseDto;
import com.example.courseenrollmentsystem.DTO.SectionDto.SectionUpdateRequestDto;
import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import com.example.courseenrollmentsystem.Service.services.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;


    // CREATE

    @PostMapping
    public ResponseEntity<SectionResponseDto> createSection(
            @Valid
            @RequestBody SectionRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        sectionService.createSection(request)
                );
    }


    // GET ALL

    @GetMapping
    public ResponseEntity<List<SectionResponseDto>>
    getAllSections() {

        return ResponseEntity.ok(
                sectionService.getAllSections()
        );
    }


    // GET BY ID

    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionResponseDto>
    getSectionById(
            @PathVariable Long sectionId
    ) {

        return ResponseEntity.ok(
                sectionService.getSectionById(sectionId)
        );
    }


    // UPDATE

    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionResponseDto>
    updateSection(
            @PathVariable Long sectionId,
            @Valid
            @RequestBody SectionUpdateRequestDto request
    ) {

        return ResponseEntity.ok(
                sectionService.updateSection(
                        sectionId,
                        request
                )
        );
    }


    // CHANGE STATUS

    @PatchMapping("/{sectionId}/status")
    public ResponseEntity<SectionResponseDto>
    changeStatus(
            @PathVariable Long sectionId,
            @RequestParam SectionStatus status
    ) {

        return ResponseEntity.ok(
                sectionService.changeStatus(
                        sectionId,
                        status
                )
        );
    }


    // GET BY PROGRAM

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<SectionResponseDto>>
    getSectionsByProgram(
            @PathVariable Long programId
    ) {

        return ResponseEntity.ok(
                sectionService
                        .getSectionsByProgram(programId)
        );
    }


    // GET BY PROGRAM + SEMESTER

    @GetMapping(
            "/program/{programId}/semester/{semesterNumber}"
    )
    public ResponseEntity<List<SectionResponseDto>>
    getSectionsByProgramAndSemester(

            @PathVariable Long programId,

            @PathVariable Integer semesterNumber
    ) {

        return ResponseEntity.ok(
                sectionService
                        .getSectionsByProgramAndSemester(
                                programId,
                                semesterNumber
                        )
        );
    }


    // PROGRAM + SEMESTER + STATUS

    @GetMapping(
            "/program/{programId}/semester/{semesterNumber}/status/{status}"
    )
    public ResponseEntity<List<SectionResponseDto>>
    getSectionsByProgramSemesterAndStatus(

            @PathVariable Long programId,

            @PathVariable Integer semesterNumber,

            @PathVariable SectionStatus status
    ) {

        return ResponseEntity.ok(
                sectionService
                        .getSectionsByProgramSemesterAndStatus(
                                programId,
                                semesterNumber,
                                status
                        )
        );
    }


    // GET BY STATUS

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SectionResponseDto>>
    getSectionsByStatus(
            @PathVariable SectionStatus status
    ) {

        return ResponseEntity.ok(
                sectionService
                        .getSectionsByStatus(status)
        );
    }
}