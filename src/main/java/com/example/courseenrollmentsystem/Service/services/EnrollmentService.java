package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.EnrollmentDto.CreateEnrollmentRequestDto;
import com.example.courseenrollmentsystem.DTO.EnrollmentDto.EnrollmentResponseDto;
import com.example.courseenrollmentsystem.Entity.*;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.*;
import com.example.courseenrollmentsystem.Repository.CourseOfferingRepository;
import com.example.courseenrollmentsystem.Repository.EnrollmentRepository;
import com.example.courseenrollmentsystem.Repository.StudentRepository;
import com.example.courseenrollmentsystem.Repository.StudentSemesterRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final StudentSemesterRepository studentSemesterRepository;

    private final CourseOfferingRepository courseOfferingRepository;

    private final StudentRepository studentRepository;


    // =====================================================
    // CREATE ENROLLMENT
    // =====================================================

    @Transactional
    public EnrollmentResponseDto createEnrollment(
            CreateEnrollmentRequestDto request
    ) {

        // -----------------------------------------
        // FIND STUDENT SEMESTER
        // -----------------------------------------

        StudentSemester studentSemester =
                studentSemesterRepository
                        .findById(request.getStudentSemesterId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student semester not found with id: "
                                                + request.getStudentSemesterId()
                                )
                        );


        // -----------------------------------------
        // FIND COURSE OFFERING
        // -----------------------------------------

        CourseOffering courseOffering =
                courseOfferingRepository
                        .findById(request.getCourseOfferingId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course offering not found with id: "
                                                + request.getCourseOfferingId()
                                )
                        );


        Student student =
                studentSemester.getStudent();


        ProgramCourse programCourse =
                courseOffering.getProgramCourse();


        AcademicSemster academicSemester =
                courseOffering.getAcademicSemester();


        LocalDate today = LocalDate.now();


        // =================================================
        // VALIDATION 1
        // STUDENT MUST BE ACTIVE
        // =================================================

        if (student.getStatus() != StudentStatus.ACTIVE) {

            throw new StudentNotEligibleException(
                    "Only active students can enroll"
            );
        }


        // =================================================
        // VALIDATION 2
        // STUDENT SEMESTER MUST BE REGISTERED
        // =================================================

        if (studentSemester.getRegistrationStatus()
                != StudentSemesterStatus.REGISTERED) {

            throw new StudentNotEligibleException(
                    "Student semester is not registered"
            );
        }


        // =================================================
        // VALIDATION 3
        // COURSE OFFERING MUST BE OPEN
        // =================================================

        if (courseOffering.getStatus()
                != CourseOfferingStatus.OPEN) {

            throw new EnrollmentClosedException(
                    "Course offering is not open for enrollment"
            );
        }


        // =================================================
        // VALIDATION 4
        // STUDENT SEMESTER ACADEMIC SEMESTER
        // MUST MATCH COURSE OFFERING SEMESTER
        // =================================================

        Long studentAcademicSemesterId =
                studentSemester
                        .getAcademicSemester()
                        .getAcademicSemsterId();


        Long offeringAcademicSemesterId =
                academicSemester
                        .getAcademicSemsterId();


        if (!studentAcademicSemesterId
                .equals(offeringAcademicSemesterId)) {

            throw new StudentNotEligibleException(
                    "Student semester and course offering academic semester do not match"
            );
        }


        // =================================================
        // VALIDATION 5
        // ENROLLMENT START DATE
        // =================================================

        if (today.isBefore(
                academicSemester.getEnrollmentStartDate()
        )) {

            throw new EnrollmentClosedException(
                    "Enrollment has not started yet"
            );
        }


        // =================================================
        // VALIDATION 6
        // ENROLLMENT END DATE
        // =================================================

        if (today.isAfter(
                academicSemester.getEnrollmentEndDate()
        )) {

            throw new EnrollmentClosedException(
                    "Enrollment deadline has passed"
            );
        }


        // =================================================
        // VALIDATION 7
        // PROGRAM MUST MATCH
        // =================================================

        Long studentProgramId =
                student
                        .getProgram()
                        .getProgramId();


        Long offeringProgramId =
                programCourse
                        .getPrograms()
                        .getProgramId();


        if (!studentProgramId.equals(offeringProgramId)) {

            throw new StudentNotEligibleException(
                    "Student program does not match course program"
            );
        }


        // =================================================
        // VALIDATION 8
        // SEMESTER NUMBER MUST MATCH
        // =================================================

        Integer studentSemesterNumber =
                studentSemester.getSemesterNumber();


        Integer courseSemesterNumber =
                programCourse.getRecommendedSemester();


        if (!studentSemesterNumber
                .equals(courseSemesterNumber)) {

            throw new StudentNotEligibleException(
                    "Student semester number does not match course semester"
            );
        }


        // =================================================
        // VALIDATION 9
        // DUPLICATE ENROLLMENT
        // =================================================

        boolean alreadyExists =
                enrollmentRepository
                        .existsByStudentSemester_StudentSemesterIdAndCourseOffering_CourseOfferingId(
                                request.getStudentSemesterId(),
                                request.getCourseOfferingId()
                        );


        if (alreadyExists) {

            throw new DublicateNotAllowedException(
                    "Student already has an enrollment record for this course offering"
            );
        }


        // =================================================
        // VALIDATION 10
        // CAPACITY CHECK
        // =================================================

        long enrolledStudents =
                enrollmentRepository
                        .countByCourseOffering_CourseOfferingIdAndStatus(
                                courseOffering.getCourseOfferingId(),
                                EnrollmentStatus.ENROLLED
                        );


        if (enrolledStudents
                >= courseOffering.getCapacity()) {

            throw new CourseOfferingFullException(
                    "Course offering has reached maximum capacity"
            );
        }


        // =================================================
        // CREATE
        // =================================================

        Enrollment enrollment =
                Enrollment.builder()

                        .studentSemester(studentSemester)

                        .courseOffering(courseOffering)

                        .enrollmentDate(today)

                        .dropDate(null)

                        .status(
                                EnrollmentStatus.ENROLLED
                        )

                        .build();


        Enrollment saved =
                enrollmentRepository.save(enrollment);


        return mapToResponseDto(saved);
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public EnrollmentResponseDto getEnrollmentById(
            Long enrollmentId
    ) {

        return mapToResponseDto(
                findEnrollmentById(enrollmentId)
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<EnrollmentResponseDto> getAllEnrollments() {

        return enrollmentRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY STUDENT SEMESTER
    // =====================================================

    public List<EnrollmentResponseDto>
    getByStudentSemester(
            Long studentSemesterId
    ) {

        checkStudentSemesterExists(
                studentSemesterId
        );


        return enrollmentRepository
                .findByStudentSemester_StudentSemesterId(
                        studentSemesterId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET ALL ENROLLMENTS OF STUDENT
    // =====================================================

    public List<EnrollmentResponseDto>
    getByStudent(
            Long studentId
    ) {

        checkStudentExists(studentId);


        return enrollmentRepository
                .findByStudentSemester_Student_StudentId(
                        studentId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // STUDENT + STATUS
    // =====================================================

    public List<EnrollmentResponseDto>
    getByStudentAndStatus(
            Long studentId,
            EnrollmentStatus status
    ) {

        checkStudentExists(studentId);


        return enrollmentRepository
                .findByStudentSemester_Student_StudentIdAndStatus(
                        studentId,
                        status
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY COURSE OFFERING
    // =====================================================

    public List<EnrollmentResponseDto>
    getByCourseOffering(
            Long courseOfferingId
    ) {

        checkCourseOfferingExists(
                courseOfferingId
        );


        return enrollmentRepository
                .findByCourseOffering_CourseOfferingId(
                        courseOfferingId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // COURSE OFFERING + STATUS
    // =====================================================

    public List<EnrollmentResponseDto>
    getByCourseOfferingAndStatus(
            Long courseOfferingId,
            EnrollmentStatus status
    ) {

        checkCourseOfferingExists(
                courseOfferingId
        );


        return enrollmentRepository
                .findByCourseOffering_CourseOfferingIdAndStatus(
                        courseOfferingId,
                        status
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // DROP ENROLLMENT
    // =====================================================

    @Transactional
    public EnrollmentResponseDto dropEnrollment(
            Long enrollmentId
    ) {

        Enrollment enrollment =
                findEnrollmentById(enrollmentId);


        // Only currently enrolled course can be dropped

        if (enrollment.getStatus()
                != EnrollmentStatus.ENROLLED) {

            throw new StudentNotEligibleException(
                    "Only an enrolled course can be dropped"
            );
        }


        AcademicSemster academicSemester =
                enrollment
                        .getCourseOffering()
                        .getAcademicSemester();


        LocalDate today =
                LocalDate.now();


        // Drop deadline

        if (today.isAfter(
                academicSemester.getDropDeadline()
        )) {

            throw new DropDeadlinePassedException(
                    "Course drop deadline has passed"
            );
        }


        enrollment.setStatus(
                EnrollmentStatus.DROPPED
        );


        enrollment.setDropDate(today);


        Enrollment updated =
                enrollmentRepository.save(enrollment);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // PRIVATE FIND ENROLLMENT
    // =====================================================

    private Enrollment findEnrollmentById(
            Long enrollmentId
    ) {

        return enrollmentRepository
                .findById(enrollmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enrollment not found with id: "
                                        + enrollmentId
                        )
                );
    }


    // =====================================================
    // CHECK STUDENT SEMESTER
    // =====================================================

    private void checkStudentSemesterExists(
            Long studentSemesterId
    ) {

        if (!studentSemesterRepository
                .existsById(studentSemesterId)) {

            throw new ResourceNotFoundException(
                    "Student semester not found with id: "
                            + studentSemesterId
            );
        }
    }


    // =====================================================
    // CHECK STUDENT
    // =====================================================

    private void checkStudentExists(
            Long studentId
    ) {

        if (!studentRepository
                .existsById(studentId)) {

            throw new ResourceNotFoundException(
                    "Student not found with id: "
                            + studentId
            );
        }
    }


    // =====================================================
    // CHECK COURSE OFFERING
    // =====================================================

    private void checkCourseOfferingExists(
            Long courseOfferingId
    ) {

        if (!courseOfferingRepository
                .existsById(courseOfferingId)) {

            throw new ResourceNotFoundException(
                    "Course offering not found with id: "
                            + courseOfferingId
            );
        }
    }


    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    private EnrollmentResponseDto mapToResponseDto(
            Enrollment enrollment
    ) {

        StudentSemester studentSemester =
                enrollment.getStudentSemester();


        Student student =
                studentSemester.getStudent();


        CourseOffering offering =
                enrollment.getCourseOffering();


        ProgramCourse programCourse =
                offering.getProgramCourse();


        Program program =
                programCourse.getPrograms();


        Course course =
                programCourse.getCourses();


        AcademicSemster semester =
                offering.getAcademicSemester();


        Teacher teacher =
                offering.getTeacher();


        Section section =
                offering.getSection();


        return EnrollmentResponseDto.builder()

                .enrollmentId(
                        enrollment.getEnrollmentId()
                )

                .studentSemesterId(
                        studentSemester.getStudentSemesterId()
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

                .semesterNumber(
                        studentSemester.getSemesterNumber()
                )

                .courseOfferingId(
                        offering.getCourseOfferingId()
                )

                .programId(
                        program.getProgramId()
                )

                .programName(
                        program.getProgramName()
                )

                .courseId(
                        course.getId()
                )

                .courseCode(
                        course.getCourseCode()
                )

                .courseName(
                        course.getCourseName()
                )

                .creditHours(
                        programCourse.getCreditHours()
                )

                .academicSemesterId(
                        semester.getAcademicSemsterId()
                )

                .academicSemesterName(
                        semester.getSemesterName()
                )

                .teacherId(
                        teacher.getTeacherId()
                )

                .teacherName(
                        teacher.getTeacherFirstName()
                                + " "
                                + teacher.getTeacherLastName()
                )

                .sectionId(
                        section.getSectionId()
                )

                .sectionCode(
                        section.getSectionCode()
                )

                .enrollmentDate(
                        enrollment.getEnrollmentDate()
                )

                .dropDate(
                        enrollment.getDropDate()
                )

                .status(
                        enrollment.getStatus()
                )

                .build();
    }
}