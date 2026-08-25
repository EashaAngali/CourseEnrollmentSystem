package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.StudentSemsterDto.CreateStudentSemesterRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentSemsterDto.StudentSemesterResponseDto;
import com.example.courseenrollmentsystem.DTO.StudentSemsterDto.UpdateStudentSemesterRequestDto;
import com.example.courseenrollmentsystem.Entity.AcademicSemster;
import com.example.courseenrollmentsystem.Entity.Program;
import com.example.courseenrollmentsystem.Entity.Student;
import com.example.courseenrollmentsystem.Entity.StudentSemester;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.AcademicSemsterRepository;
import com.example.courseenrollmentsystem.Repository.StudentRepository;
import com.example.courseenrollmentsystem.Repository.StudentSemesterRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentSemesterService {


    private final StudentSemesterRepository studentSemesterRepository;

    private final StudentRepository studentRepository;

    private final AcademicSemsterRepository academicSemesterRepository;


    // =====================================================
    // CREATE STUDENT SEMESTER
    // =====================================================

    @Transactional
    public StudentSemesterResponseDto createStudentSemester(
            CreateStudentSemesterRequestDto request
    ) {


        // ---------------------------------------------
        // FIND STUDENT
        // ---------------------------------------------

        Student student =
                studentRepository
                        .findById(request.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + request.getStudentId()
                                )
                        );


        // ---------------------------------------------
        // STUDENT MUST BE ACTIVE
        // ---------------------------------------------

        if (student.getStatus() != StudentStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Only active students can be registered for a semester"
            );
        }


        // ---------------------------------------------
        // FIND ACADEMIC SEMESTER
        // ---------------------------------------------

        AcademicSemster academicSemester =
                academicSemesterRepository
                        .findById(request.getAcademicSemesterId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic semester not found with id: "
                                                + request.getAcademicSemesterId()
                                )
                        );


        // ---------------------------------------------
        // DUPLICATE CHECK
        //
        // Same student cannot have two records for
        // same AcademicSemester
        // ---------------------------------------------

        boolean alreadyExists =
                studentSemesterRepository
                        .existsByStudent_StudentIdAndAcademicSemester_AcademicSemsterId(
                                request.getStudentId(),
                                request.getAcademicSemesterId()
                        );


        if (alreadyExists) {

            throw new DublicateNotAllowedException(
                    "Student semester record already exists for this academic semester"
            );
        }


        // ---------------------------------------------
        // SEMESTER NUMBER VALIDATION
        //
        // Student Program.totalSemesters ke against
        // ---------------------------------------------

        Integer totalSemesters =
                student
                        .getProgram()
                        .getTotalSemster();


        if (request.getSemesterNumber() < 1
                || request.getSemesterNumber() > totalSemesters) {

            throw new InvalidResourceException(
                    "Semester number must be between 1 and "
                            + totalSemesters
            );
        }


        // ---------------------------------------------
        // CREATE
        //
        // Initially fee pending.
        // Later voucher/payment module REGISTERED karega.
        // ---------------------------------------------

        StudentSemester studentSemester =
                StudentSemester.builder()

                        .student(student)

                        .academicSemester(
                                academicSemester
                        )

                        .semesterNumber(
                                request.getSemesterNumber()
                        )

                        .registrationStatus(
                                StudentSemesterStatus.FEE_PENDING
                        )

                        .semesterGpa(null)

                        .cumulativeGpa(null)

                        .build();


        StudentSemester saved =
                studentSemesterRepository
                        .save(studentSemester);


        return mapToResponseDto(saved);
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public StudentSemesterResponseDto getStudentSemesterById(
            Long studentSemesterId
    ) {

        StudentSemester studentSemester =
                findStudentSemesterById(
                        studentSemesterId
                );


        return mapToResponseDto(studentSemester);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<StudentSemesterResponseDto>
    getAllStudentSemesters() {

        return studentSemesterRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET ALL SEMESTERS OF ONE STUDENT
    // =====================================================

    public List<StudentSemesterResponseDto>
    getByStudent(
            Long studentId
    ) {

        checkStudentExists(studentId);


        return studentSemesterRepository
                .findByStudent_StudentId(studentId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET ALL STUDENTS OF ONE ACADEMIC SEMESTER
    // =====================================================

    public List<StudentSemesterResponseDto>
    getByAcademicSemester(
            Long academicSemesterId
    ) {

        checkAcademicSemesterExists(
                academicSemesterId
        );


        return studentSemesterRepository
                .findByAcademicSemester_AcademicSemsterId(
                        academicSemesterId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET STUDENT + ACADEMIC SEMESTER
    // =====================================================

    public StudentSemesterResponseDto
    getByStudentAndAcademicSemester(
            Long studentId,
            Long academicSemesterId
    ) {

        StudentSemester studentSemester =
                studentSemesterRepository
                        .findByStudent_StudentIdAndAcademicSemester_AcademicSemsterId(
                                studentId,
                                academicSemesterId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student semester record not found"
                                )
                        );


        return mapToResponseDto(studentSemester);
    }


    // =====================================================
    // GET BY STATUS
    // =====================================================

    public List<StudentSemesterResponseDto>
    getByRegistrationStatus(
            StudentSemesterStatus status
    ) {

        return studentSemesterRepository
                .findByRegistrationStatus(status)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET STUDENT SEMESTERS BY STATUS
    // =====================================================

    public List<StudentSemesterResponseDto>
    getStudentSemestersByStatus(
            Long studentId,
            StudentSemesterStatus status
    ) {

        checkStudentExists(studentId);


        return studentSemesterRepository
                .findByStudent_StudentIdAndRegistrationStatus(
                        studentId,
                        status
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public StudentSemesterResponseDto updateStudentSemester(
            Long studentSemesterId,
            UpdateStudentSemesterRequestDto request
    ) {

        StudentSemester studentSemester =
                findStudentSemesterById(
                        studentSemesterId
                );


        // Registration status

        studentSemester.setRegistrationStatus(
                request.getRegistrationStatus()
        );


        // GPA validation

        validateGpa(
                request.getSemesterGpa(),
                "Semester GPA"
        );


        validateGpa(
                request.getCumulativeGpa(),
                "Cumulative GPA"
        );


        studentSemester.setSemesterGpa(
                request.getSemesterGpa()
        );


        studentSemester.setCumulativeGpa(
                request.getCumulativeGpa()
        );


        StudentSemester updated =
                studentSemesterRepository
                        .save(studentSemester);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // CHANGE REGISTRATION STATUS
    // =====================================================

    @Transactional
    public StudentSemesterResponseDto changeRegistrationStatus(
            Long studentSemesterId,
            StudentSemesterStatus status
    ) {

        StudentSemester studentSemester =
                findStudentSemesterById(
                        studentSemesterId
                );


        studentSemester.setRegistrationStatus(
                status
        );


        StudentSemester updated =
                studentSemesterRepository
                        .save(studentSemester);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // PRIVATE: FIND
    // =====================================================

    private StudentSemester findStudentSemesterById(
            Long studentSemesterId
    ) {

        return studentSemesterRepository
                .findById(studentSemesterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student semester not found with id: "
                                        + studentSemesterId
                        )
                );
    }


    // =====================================================
    // PRIVATE: STUDENT EXISTS
    // =====================================================

    private void checkStudentExists(
            Long studentId
    ) {

        if (!studentRepository.existsById(studentId)) {

            throw new ResourceNotFoundException(
                    "Student not found with id: "
                            + studentId
            );
        }
    }


    // =====================================================
    // PRIVATE: ACADEMIC SEMESTER EXISTS
    // =====================================================

    private void checkAcademicSemesterExists(
            Long academicSemesterId
    ) {

        if (!academicSemesterRepository
                .existsById(academicSemesterId)) {

            throw new ResourceNotFoundException(
                    "Academic semester not found with id: "
                            + academicSemesterId
            );
        }
    }


    // =====================================================
    // PRIVATE: GPA VALIDATION
    // =====================================================

    private void validateGpa(
            Double gpa,
            String fieldName
    ) {

        if (gpa != null
                && (gpa < 0.0 || gpa > 4.0)) {

            throw new InvalidResourceException(
                    fieldName
                            + " must be between 0.0 and 4.0"
            );
        }
    }


    // =====================================================
    // PRIVATE: ENTITY -> DTO
    // =====================================================

    private StudentSemesterResponseDto mapToResponseDto(
            StudentSemester studentSemester
    ) {

        Student student =
                studentSemester.getStudent();


        Program program =
                student.getProgram();


        AcademicSemster academicSemester =
                studentSemester
                        .getAcademicSemester();


        return StudentSemesterResponseDto.builder()

                .studentSemesterId(
                        studentSemester
                                .getStudentSemesterId()
                )

                .studentId(
                        student.getStudentId()
                )

                .studentNumber(
                        student.getStudentNumber()
                )

                .studentName(
                        student.getFirstName()
                                + " "
                                + student.getLastName()
                )

                .programId(
                        program.getProgramId()
                )

                .programName(
                        program.getProgramName()
                )

                .academicSemesterId(
                        academicSemester
                                .getAcademicSemsterId()
                )

                .academicSemesterName(
                        academicSemester
                                .getSemesterName()
                )

                .semesterNumber(
                        studentSemester
                                .getSemesterNumber()
                )

                .registrationStatus(
                        studentSemester
                                .getRegistrationStatus()
                )

                .semesterGpa(
                        studentSemester
                                .getSemesterGpa()
                )

                .cumulativeGpa(
                        studentSemester
                                .getCumulativeGpa()
                )

                .build();
    }
}