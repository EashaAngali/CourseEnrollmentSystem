package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.SectionDto.SectionRequestDto;
import com.example.courseenrollmentsystem.DTO.SectionDto.SectionResponseDto;
import com.example.courseenrollmentsystem.DTO.SectionDto.SectionUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Program;
import com.example.courseenrollmentsystem.Entity.Section;
import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.ProgramRepository;
import com.example.courseenrollmentsystem.Repository.SectionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class SectionService {

        private final SectionRepository sectionRepository;
        private final ProgramRepository programRepository;


        // ==============================
        // CREATE
        // ==============================

        @Transactional
        public SectionResponseDto createSection(
                SectionRequestDto request
        ) {

            Program program = programRepository
                    .findById(request.getProgramId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Program not found with id: "
                                            + request.getProgramId()
                            )
                    );

            validateSemester(
                    request.getSemesterNumber(),
                    program
            );

            boolean duplicate =
                    sectionRepository
                            .existsByProgram_ProgramIdAndSemesterNumberAndSectionNameIgnoreCase(
                                    request.getProgramId(),
                                    request.getSemesterNumber(),
                                    request.getSectionName()
                            );

            if (duplicate) {
                throw new DublicateNotAllowedException(
                        "Section "
                                + request.getSectionName()
                                + " already exists in semester "
                                + request.getSemesterNumber()
                                + " for program "
                                + program.getProgramName()
                );
            }

            String sectionCode = generateSectionCode(
                    program,
                    request.getSemesterNumber(),
                    request.getSectionName()
            );

            Section section = Section.builder()
                    .sectionName(
                            request.getSectionName().trim().toUpperCase()
                    )
                    .sectionCode(sectionCode)
                    .semesterNumber(
                            request.getSemesterNumber()
                    )
                    .capacity(
                            request.getCapacity()
                    )
                    .status(SectionStatus.ACTIVE)
                    .program(program)
                    .build();

            Section savedSection =
                    sectionRepository.save(section);

            return mapToResponse(savedSection);
        }


        // ==============================
        // GET BY ID
        // ==============================

        public SectionResponseDto getSectionById(Long sectionId) {

            Section section = findSection(sectionId);

            return mapToResponse(section);
        }


        // ==============================
        // GET ALL
        // ==============================

        public List<SectionResponseDto> getAllSections() {

            return sectionRepository
                    .findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // ==============================
        // UPDATE
        // ==============================

        @Transactional
        public SectionResponseDto updateSection(
                Long sectionId,
                 SectionUpdateRequestDto request
        ) {

            Section section = findSection(sectionId);

            Program program = section.getProgram();

            validateSemester(
                    request.getSemesterNumber(),
                    program
            );

            boolean duplicate =
                    sectionRepository
                            .existsByProgram_ProgramIdAndSemesterNumberAndSectionNameIgnoreCase(
                                    program.getProgramId(),
                                    request.getSemesterNumber(),
                                    request.getSectionName()
                            );

            boolean sameSection =
                    section.getSectionName()
                            .equalsIgnoreCase(
                                    request.getSectionName()
                            )
                            &&
                            section.getSemesterNumber()
                                    .equals(
                                            request.getSemesterNumber()
                                    );

            if (duplicate && !sameSection) {

                throw new DublicateNotAllowedException(
                        "Section already exists for this program and semester"
                );
            }

            section.setSectionName(
                    request.getSectionName()
                            .trim()
                            .toUpperCase()
            );

            section.setSemesterNumber(
                    request.getSemesterNumber()
            );

            section.setCapacity(
                    request.getCapacity()
            );

            section.setStatus(
                    request.getStatus()
            );

            section.setSectionCode(
                    generateSectionCode(
                            program,
                            request.getSemesterNumber(),
                            request.getSectionName()
                    )
            );

            Section updatedSection =
                    sectionRepository.save(section);

            return mapToResponse(updatedSection);
        }


        // ==============================
        // CHANGE STATUS
        // ==============================

        @Transactional
        public SectionResponseDto changeStatus(
                Long sectionId,
                SectionStatus status
        ) {

            Section section = findSection(sectionId);

            section.setStatus(status);

            Section updated =
                    sectionRepository.save(section);

            return mapToResponse(updated);
        }


        // ==============================
        // GET BY PROGRAM
        // ==============================

        public List<SectionResponseDto> getSectionsByProgram(
                Long programId
        ) {

            checkProgramExists(programId);

            return sectionRepository
                    .findByProgram_ProgramId(programId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // ==============================
        // GET BY PROGRAM + SEMESTER
        // ==============================

        public List<SectionResponseDto>
        getSectionsByProgramAndSemester(
                Long programId,
                Integer semesterNumber
        ) {

            Program program = programRepository
                    .findById(programId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Program not found with id: "
                                            + programId
                            )
                    );

            validateSemester(
                    semesterNumber,
                    program
            );

            return sectionRepository
                    .findByProgram_ProgramIdAndSemesterNumber(
                            programId,
                            semesterNumber
                    )
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // ==============================
        // PROGRAM + SEMESTER + STATUS
        // ==============================

        public List<SectionResponseDto>
        getSectionsByProgramSemesterAndStatus(
                Long programId,
                Integer semesterNumber,
                SectionStatus status
        ) {

            Program program = programRepository
                    .findById(programId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Program not found with id: "
                                            + programId
                            )
                    );

            validateSemester(
                    semesterNumber,
                    program
            );

            return sectionRepository
                    .findByProgram_ProgramIdAndSemesterNumberAndStatus(
                            programId,
                            semesterNumber,
                            status
                    )
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // ==============================
        // GET BY STATUS
        // ==============================

        public List<SectionResponseDto> getSectionsByStatus(
                SectionStatus status
        ) {

            return sectionRepository
                    .findByStatus(status)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // ==============================
        // PRIVATE HELPER
        // ==============================

        private Section findSection(Long sectionId) {

            return sectionRepository
                    .findById(sectionId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Section not found with id: "
                                            + sectionId
                            )
                    );
        }


        private void checkProgramExists(Long programId) {

            if (!programRepository.existsById(programId)) {

                throw new ResourceNotFoundException(
                        "Program not found with id: "
                                + programId
                );
            }
        }


        private void validateSemester(
                Integer semesterNumber,
                Program program
        ) {

            if (semesterNumber == null
                    || semesterNumber < 1
                    || semesterNumber > program.getTotalSemster()) {

                throw new ResourceNotFoundException(
                        "Semester number must be between 1 and "
                                + program.getTotalSemster()
                );
            }
        }


        private String generateSectionCode(
                Program program,
                Integer semesterNumber,
                String sectionName
        ) {

            return program.getProgramCode()
                    .toUpperCase()
                    + "-"
                    + semesterNumber
                    + sectionName
                    .trim()
                    .toUpperCase();
        }


        private SectionResponseDto mapToResponse(
                Section section
        ) {

            return SectionResponseDto.builder()

                    .sectionId(
                            section.getSectionId()
                    )

                    .sectionName(
                            section.getSectionName()
                    )

                    .sectionCode(
                            section.getSectionCode()
                    )

                    .programId(
                            section.getProgram()
                                    .getProgramId()
                    )

                    .programName(
                            section.getProgram()
                                    .getProgramName()
                    )

                    .semesterNumber(
                            section.getSemesterNumber()
                    )

                    .capacity(
                            section.getCapacity()
                    )

                    .status(
                            section.getStatus()
                    )

                    .build();
        }
    }
