package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.AttendanceDto.*;
import com.example.courseenrollmentsystem.Entity.*;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceMethod;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import com.example.courseenrollmentsystem.Enum.EnrollmentEnum.EnrollmentStatus;
import com.example.courseenrollmentsystem.Enum.StudentCard.StudentCardStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.AttendanceAlreadyMarkedException;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.AttendanceRepository;
import com.example.courseenrollmentsystem.Repository.ClassSessionRepository;
import com.example.courseenrollmentsystem.Repository.EnrollmentRepository;
import com.example.courseenrollmentsystem.Repository.StudentCardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AttendanceService {


    private final AttendanceRepository attendanceRepository;

    private final ClassSessionRepository classSessionRepository;

    private final StudentCardRepository studentCardRepository;

    private final EnrollmentRepository enrollmentRepository;


    // =====================================================
    // QR ATTENDANCE
    // =====================================================

    @Transactional
    public AttendanceResponseDto markAttendanceByQr(
            CreateAttendanceRequestDto request
    ) {

        // -------------------------------------------------
        // 1. FIND CLASS SESSION
        // -------------------------------------------------

        ClassSession classSession =
                classSessionRepository
                        .findById(request.getClassSessionId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Class session not found with id: "
                                                + request.getClassSessionId()
                                )
                        );


        // -------------------------------------------------
        // 2. SESSION MUST BE ACTIVE
        // -------------------------------------------------

        if (classSession.getStatus()
                != ClassSessionStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Attendance can only be marked for an active class session"
            );
        }


        // -------------------------------------------------
        // 3. FIND STUDENT CARD USING QR TOKEN
        // -------------------------------------------------

        StudentCard studentCard =
                studentCardRepository
                        .findByQrToken(
                                request.getQrToken()
                        )
                        .orElseThrow(() ->
                                new InvalidResourceException(
                                        "Invalid QR code"
                                )
                        );


        // -------------------------------------------------
        // 4. CHECK STUDENT CARD STATUS
        // -------------------------------------------------

        if (studentCard.getStatus()
                != StudentCardStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Student card is not active"
            );
        }


        // -------------------------------------------------
        // 5. CHECK CARD EXPIRY
        // -------------------------------------------------

        if (studentCard.getExpiryDate() != null
                && studentCard.getExpiryDate()
                .isBefore(LocalDate.now())) {

            throw new InvalidResourceException(
                    "Student card has expired"
            );
        }


        // -------------------------------------------------
        // 6. GET STUDENT FROM CARD
        // -------------------------------------------------

        Student student =
                studentCard.getStudent();


        /*
         * If your Student entity has an ACTIVE/INACTIVE status,
         * enable this validation and adjust enum names.
         *
         * if (student.getStatus() != StudentStatus.ACTIVE) {
         *     throw new InvalidAttendanceException(
         *         "Student is not active"
         *     );
         * }
         */


        // -------------------------------------------------
        // 7. GET COURSE OFFERING FROM CLASS SESSION
        // -------------------------------------------------

        Long courseOfferingId =
                classSession
                        .getCourseOffering()
                        .getCourseOfferingId();


        // -------------------------------------------------
        // 8. FIND STUDENT'S ACTIVE ENROLLMENT
        //
        // Student from QR
        // +
        // CourseOffering from ClassSession
        // -------------------------------------------------

        Enrollment enrollment =
                enrollmentRepository
                        .findByStudentSemester_Student_StudentIdAndCourseOffering_CourseOfferingIdAndStatus(
                                student.getStudentId(),
                                courseOfferingId,
                                EnrollmentStatus.ENROLLED
                        )
                        .orElseThrow(() ->
                                new InvalidResourceException(
                                        "Student is not enrolled in this class"
                                )
                        );


        // -------------------------------------------------
        // 9. DUPLICATE ATTENDANCE CHECK
        // -------------------------------------------------

        boolean alreadyMarked =
                attendanceRepository
                        .existsByClassSession_ClassSessionIdAndEnrollment_EnrollmentId(
                                classSession.getClassSessionId(),
                                enrollment.getEnrollmentId()
                        );


        if (alreadyMarked) {

            throw new AttendanceAlreadyMarkedException(
                    "Attendance has already been marked for this student"
            );
        }


        // -------------------------------------------------
        // 10. CREATE ATTENDANCE
        // -------------------------------------------------

        Attendance attendance =
                Attendance.builder()

                        .classSession(
                                classSession
                        )

                        .enrollment(
                                enrollment
                        )

                        .attendanceStatus(
                                AttendanceStatus.PRESENT
                        )

                        .attendanceMethod(
                                AttendanceMethod.QR_CARD
                        )

                        .attendanceDate(
                                LocalDateTime.now()
                        )

                        .build();


        Attendance saved =
                attendanceRepository
                        .save(attendance);


        return mapToResponseDto(saved);
    }


    // =====================================================
    // MANUAL ATTENDANCE
    // =====================================================

    @Transactional
    public AttendanceResponseDto markManualAttendance(
            ManualAttendanceRequestDto request
    ) {

        // -------------------------------------------------
        // CLASS SESSION
        // -------------------------------------------------

        ClassSession classSession =
                classSessionRepository
                        .findById(request.getClassSessionId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Class session not found with id: "
                                                + request.getClassSessionId()
                                )
                        );


        // -------------------------------------------------
        // CANCELLED SESSION CANNOT HAVE ATTENDANCE
        // -------------------------------------------------

        if (classSession.getStatus()
                == ClassSessionStatus.CANCELLED) {

            throw new InvalidResourceException(
                    "Attendance cannot be marked for a cancelled class session"
            );
        }


        // -------------------------------------------------
        // FIND ENROLLMENT
        // -------------------------------------------------

        Enrollment enrollment =
                enrollmentRepository
                        .findById(request.getEnrollmentId())
                        .orElseThrow(() ->
                                new InvalidResourceException(
                                        "Enrollment not found with id: "
                                                + request.getEnrollmentId()
                                )
                        );


        // -------------------------------------------------
        // ENROLLMENT MUST BE ENROLLED
        // -------------------------------------------------

        if (enrollment.getStatus()
                != EnrollmentStatus.ENROLLED) {

            throw new InvalidResourceException(
                    "Student does not have an active enrollment"
            );
        }


        // -------------------------------------------------
        // VERY IMPORTANT:
        // Enrollment must belong to SAME CourseOffering
        // as ClassSession
        // -------------------------------------------------

        Long enrollmentOfferingId =
                enrollment
                        .getCourseOffering()
                        .getCourseOfferingId();


        Long sessionOfferingId =
                classSession
                        .getCourseOffering()
                        .getCourseOfferingId();


        if (!enrollmentOfferingId
                .equals(sessionOfferingId)) {

            throw new InvalidResourceException(
                    "Enrollment does not belong to this class"
            );
        }


        // -------------------------------------------------
        // DUPLICATE
        // -------------------------------------------------

        boolean alreadyMarked =
                attendanceRepository
                        .existsByClassSession_ClassSessionIdAndEnrollment_EnrollmentId(
                                classSession.getClassSessionId(),
                                enrollment.getEnrollmentId()
                        );


        if (alreadyMarked) {

            throw new AttendanceAlreadyMarkedException(
                    "Attendance has already been marked for this student"
            );
        }


        // -------------------------------------------------
        // SAVE
        // -------------------------------------------------

        Attendance attendance =
                Attendance.builder()

                        .classSession(
                                classSession
                        )

                        .enrollment(
                                enrollment
                        )

                        .attendanceStatus(
                                request.getStatus()
                        )

                        .attendanceMethod(
                                AttendanceMethod.MANUAL
                        )

                        .attendanceDate(
                                LocalDateTime.now()
                        )

                        .build();


        return mapToResponseDto(
                attendanceRepository.save(
                        attendance
                )
        );
    }


    // =====================================================
    // UPDATE ATTENDANCE
    // =====================================================

    @Transactional
    public AttendanceResponseDto updateAttendance(
            Long attendanceId,
            UpdateAttendanceRequestDto request
    ) {

        Attendance attendance =
                findAttendanceById(
                        attendanceId
                );


        // Cancelled class attendance shouldn't be modified

        if (attendance
                .getClassSession()
                .getStatus()
                == ClassSessionStatus.CANCELLED) {

            throw new InvalidResourceException(
                    "Attendance of a cancelled class session cannot be updated"
            );
        }


        attendance.setAttendanceStatus(
                request.getStatus()
        );


        /*
         * Because teacher/admin manually corrected it,
         * method becomes MANUAL.
         */

        attendance.setAttendanceMethod(
                AttendanceMethod.MANUAL
        );


        attendance.setAttendanceDate(
                LocalDateTime.now()
        );


        return mapToResponseDto(
                attendanceRepository
                        .save(attendance)
        );
    }


    // =====================================================
    // GET ATTENDANCE BY ID
    // =====================================================

    public AttendanceResponseDto getAttendanceById(
            Long attendanceId
    ) {

        return mapToResponseDto(
                findAttendanceById(
                        attendanceId
                )
        );
    }


    // =====================================================
    // GET ATTENDANCE BY CLASS SESSION
    // =====================================================

    public List<AttendanceResponseDto>
    getByClassSession(
            Long classSessionId
    ) {

        if (!classSessionRepository
                .existsById(classSessionId)) {

            throw new ResourceNotFoundException(
                    "Class session not found with id: "
                            + classSessionId
            );
        }


        return attendanceRepository
                .findByClassSession_ClassSessionId(
                        classSessionId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET ATTENDANCE BY ENROLLMENT
    // =====================================================

    public List<AttendanceResponseDto>
    getByEnrollment(
            Long enrollmentId
    ) {

        if (!enrollmentRepository
                .existsById(enrollmentId)) {

            throw new InvalidResourceException(
                    "Enrollment not found with id: "
                            + enrollmentId
            );
        }


        return attendanceRepository
                .findByEnrollment_EnrollmentId(
                        enrollmentId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET STUDENT ATTENDANCE
    // =====================================================

    public List<AttendanceResponseDto>
    getByStudent(
            Long studentId
    ) {

        return attendanceRepository
                .findByEnrollment_StudentSemester_Student_StudentId(
                        studentId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET COURSE OFFERING ATTENDANCE
    // =====================================================

    public List<AttendanceResponseDto>
    getByCourseOffering(
            Long courseOfferingId
    ) {

        return attendanceRepository
                .findByClassSession_CourseOffering_CourseOfferingId(
                        courseOfferingId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // ATTENDANCE SUMMARY
    // =====================================================

    public AttendanceSummaryResponseDto getAttendanceSummary(
            Long enrollmentId
    ) {

        Enrollment enrollment =
                enrollmentRepository
                        .findById(enrollmentId)
                        .orElseThrow(() ->
                                new InvalidResourceException(
                                        "Enrollment not found with id: "
                                                + enrollmentId
                                )
                        );


        long totalClasses =
                attendanceRepository
                        .countByEnrollment_EnrollmentId(
                                enrollmentId
                        );


        long presentCount =
                attendanceRepository
                        .countByEnrollment_EnrollmentIdAndAttendanceStatus(
                                enrollmentId,
                                AttendanceStatus.PRESENT
                        );


        long absentCount =
                attendanceRepository
                        .countByEnrollment_EnrollmentIdAndAttendanceStatus(
                                enrollmentId,
                                AttendanceStatus.ABSENT
                        );


        long lateCount =
                attendanceRepository
                        .countByEnrollment_EnrollmentIdAndAttendanceStatus(
                                enrollmentId,
                                AttendanceStatus.LATE
                        );


        long excusedCount =
                attendanceRepository
                        .countByEnrollment_EnrollmentIdAndAttendanceStatus(
                                enrollmentId,
                                AttendanceStatus.EXCUSED
                        );


        // -------------------------------------------------
        // PRESENT + LATE = ATTENDED
        // -------------------------------------------------

        long attendedClasses =
                presentCount + lateCount;


        double percentage = 0.0;


        if (totalClasses > 0) {

            percentage =
                    ((double) attendedClasses
                            / totalClasses)
                            * 100;
        }


        Student student =
                enrollment
                        .getStudentSemester()
                        .getStudent();


        CourseOffering offering =
                enrollment
                        .getCourseOffering();


        Course course =
                offering
                        .getProgramCourse()
                        .getCourses();


        return AttendanceSummaryResponseDto
                .builder()

                .enrollmentId(
                        enrollment.getEnrollmentId()
                )

                .studentId(
                        student.getStudentId()
                )

                .studentName(
                        student.getFirstName()
                                + " "
                                + student.getLastName()
                )

                .courseOfferingId(
                        offering.getCourseOfferingId()
                )

                .courseCode(
                        course.getCourseCode()
                )

                .courseName(
                        course.getCourseName()
                )

                .totalClasses(
                        totalClasses
                )

                .presentCount(
                        presentCount
                )

                .absentCount(
                        absentCount
                )

                .lateCount(
                        lateCount
                )

                .excusedCount(
                        excusedCount
                )

                .attendancePercentage(
                        percentage
                )

                .build();
    }


    // =====================================================
    // PRIVATE FIND
    // =====================================================

    private Attendance findAttendanceById(
            Long attendanceId
    ) {

        return attendanceRepository
                .findById(attendanceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance not found with id: "
                                        + attendanceId
                        )
                );
    }


    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    private AttendanceResponseDto mapToResponseDto(
            Attendance attendance
    ) {

        Enrollment enrollment =
                attendance.getEnrollment();


        Student student =
                enrollment
                        .getStudentSemester()
                        .getStudent();


        ClassSession classSession =
                attendance.getClassSession();


        CourseOffering offering =
                classSession
                        .getCourseOffering();


        Course course =
                offering
                        .getProgramCourse()
                        .getCourses();


        return AttendanceResponseDto
                .builder()

                .attendanceId(
                        attendance.getAttendanceId()
                )

                .classSessionId(
                        classSession.getClassSessionId()
                )

                .enrollmentId(
                        enrollment.getEnrollmentId()
                )

                .studentId(
                        student.getStudentId()
                )

                .studentName(
                        student.getFirstName()
                                + " "
                                + student.getLastName()
                )

                .courseOfferingId(
                        offering.getCourseOfferingId()
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

                .sessionDate(
                        classSession.getSessionDate()
                )

                .status(
                        attendance.getAttendanceStatus()
                )

                .attendanceMethod(
                        attendance.getAttendanceMethod()
                )

                .markedAt(
                        attendance.getAttendanceDate()
                )

                .build();
    }
}