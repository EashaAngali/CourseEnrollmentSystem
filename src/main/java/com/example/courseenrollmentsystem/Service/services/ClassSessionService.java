package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.ClassSessionDto.ClassSessionResponseDto;
import com.example.courseenrollmentsystem.DTO.ClassSessionDto.CreateClassSessionRequestDto;
import com.example.courseenrollmentsystem.DTO.ClassSessionDto.UpdateClassSessionRequestDto;
import com.example.courseenrollmentsystem.Entity.*;
import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.ClassSessionRepository;
import com.example.courseenrollmentsystem.Repository.CourseOfferingRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClassSessionService {


    private final ClassSessionRepository classSessionRepository;

    private final CourseOfferingRepository courseOfferingRepository;


    // =====================================================
    // CREATE CLASS SESSION
    // =====================================================

    @Transactional
    public ClassSessionResponseDto createClassSession(
            CreateClassSessionRequestDto request
    ) {

        // ---------------------------------------------
        // FIND COURSE OFFERING
        // ---------------------------------------------

        CourseOffering courseOffering =
                courseOfferingRepository
                        .findById(request.getCourseOfferingId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course offering not found with id: "
                                                + request.getCourseOfferingId()
                                )
                        );


        // ---------------------------------------------
        // COURSE OFFERING VALIDATION
        // ---------------------------------------------

        if (courseOffering.getStatus()
                == CourseOfferingStatus.CANCELLED) {

            throw new InvalidResourceException(
                    "Cannot create class session for a cancelled course offering"
            );
        }


        if (courseOffering.getStatus()
                == CourseOfferingStatus.COMPLETED) {

            throw new InvalidResourceException(
                    "Cannot create class session for a completed course offering"
            );
        }


        // ---------------------------------------------
        // DATE VALIDATION
        //
        // Session academic semester ke andar honi chahiye.
        // ---------------------------------------------

        AcademicSemster academicSemester =
                courseOffering.getAcademicSemester();


        if (request.getSessionDate()
                .isBefore(academicSemester.getStartDate())

                ||

                request.getSessionDate()
                        .isAfter(academicSemester.getEndDate())) {

            throw new InvalidResourceException(
                    "Class session date must be within the academic semester"
            );
        }


        // ---------------------------------------------
        // TIME VALIDATION
        // ---------------------------------------------

        if (!request.getEndTime()
                .isAfter(request.getStartTime())) {

            throw new InvalidResourceException(
                    "End time must be after start time"
            );
        }


        // ---------------------------------------------
        // CREATE
        // ---------------------------------------------

        ClassSession classSession =
                ClassSession.builder()

                        .courseOffering(
                                courseOffering
                        )

                        .sessionDate(
                                request.getSessionDate()
                        )

                        .startTime(
                                request.getStartTime()
                        )

                        .endTime(
                                request.getEndTime()
                        )

                        .topic(
                                request.getTopic()
                        )

                        .status(
                                ClassSessionStatus.SCHEDULED
                        )

                        .build();


        ClassSession saved =
                classSessionRepository
                        .save(classSession);


        return mapToResponseDto(saved);
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public ClassSessionResponseDto getClassSessionById(
            Long classSessionId
    ) {

        return mapToResponseDto(
                findClassSessionById(
                        classSessionId
                )
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<ClassSessionResponseDto>
    getAllClassSessions() {

        return classSessionRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // UPDATE SCHEDULED SESSION
    // =====================================================

    @Transactional
    public ClassSessionResponseDto updateClassSession(
            Long classSessionId,
            UpdateClassSessionRequestDto request
    ) {

        ClassSession classSession =
                findClassSessionById(
                        classSessionId
                );


        // ---------------------------------------------
        // ONLY SCHEDULED SESSION EDITABLE
        // ---------------------------------------------

        if (classSession.getStatus()
                != ClassSessionStatus.SCHEDULED) {

            throw new InvalidResourceException(
                    "Only scheduled class sessions can be updated"
            );
        }


        AcademicSemster academicSemester =
                classSession
                        .getCourseOffering()
                        .getAcademicSemester();


        // ---------------------------------------------
        // DATE VALIDATION
        // ---------------------------------------------

        if (request.getSessionDate()
                .isBefore(
                        academicSemester.getStartDate()
                )

                ||

                request.getSessionDate()
                        .isAfter(
                                academicSemester.getEndDate()
                        )) {

            throw new InvalidResourceException(
                    "Class session date must be within the academic semester"
            );
        }


        // ---------------------------------------------
        // TIME VALIDATION
        // ---------------------------------------------

        if (!request.getEndTime()
                .isAfter(
                        request.getStartTime()
                )) {

            throw new InvalidResourceException(
                    "End time must be after start time"
            );
        }


        classSession.setSessionDate(
                request.getSessionDate()
        );


        classSession.setStartTime(
                request.getStartTime()
        );


        classSession.setEndTime(
                request.getEndTime()
        );


        classSession.setTopic(
                request.getTopic()
        );


        ClassSession updated =
                classSessionRepository
                        .save(classSession);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // START CLASS SESSION
    // =====================================================

    @Transactional
    public ClassSessionResponseDto startClassSession(
            Long classSessionId
    ) {

        ClassSession classSession =
                findClassSessionById(
                        classSessionId
                );


        // ---------------------------------------------
        // ONLY SCHEDULED CAN START
        // ---------------------------------------------

        if (classSession.getStatus()
                != ClassSessionStatus.SCHEDULED) {

            throw new InvalidResourceException(
                    "Only scheduled class sessions can be started"
            );
        }


        // ---------------------------------------------
        // OPTIONAL BUT USEFUL:
        // Session should normally be today's session.
        // ---------------------------------------------

        if (!classSession.getSessionDate()
                .equals(LocalDate.now())) {

            throw new InvalidResourceException(
                    "Class session can only be started on its scheduled date"
            );
        }


        // ---------------------------------------------
        // PREVENT MULTIPLE ACTIVE SESSIONS
        // FOR SAME COURSE OFFERING
        // ---------------------------------------------

        boolean activeSessionExists =
                classSessionRepository
                        .existsByCourseOffering_CourseOfferingIdAndStatus(
                                classSession
                                        .getCourseOffering()
                                        .getCourseOfferingId(),

                                ClassSessionStatus.ACTIVE
                        );


        if (activeSessionExists) {

            throw new InvalidResourceException(
                    "An active class session already exists for this course offering"
            );
        }


        classSession.setStatus(
                ClassSessionStatus.ACTIVE
        );


        ClassSession updated =
                classSessionRepository
                        .save(classSession);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // COMPLETE CLASS SESSION
    // =====================================================

    @Transactional
    public ClassSessionResponseDto completeClassSession(
            Long classSessionId
    ) {

        ClassSession classSession =
                findClassSessionById(
                        classSessionId
                );


        // ---------------------------------------------
        // ONLY ACTIVE SESSION CAN COMPLETE
        // ---------------------------------------------

        if (classSession.getStatus()
                != ClassSessionStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Only an active class session can be completed"
            );
        }


        classSession.setStatus(
                ClassSessionStatus.COMPLETED
        );


        ClassSession updated =
                classSessionRepository
                        .save(classSession);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // CANCEL CLASS SESSION
    // =====================================================

    @Transactional
    public ClassSessionResponseDto cancelClassSession(
            Long classSessionId
    ) {

        ClassSession classSession =
                findClassSessionById(
                        classSessionId
                );


        // Completed session cannot be cancelled

        if (classSession.getStatus()
                == ClassSessionStatus.COMPLETED) {

            throw new InvalidResourceException(
                    "Completed class session cannot be cancelled"
            );
        }


        if (classSession.getStatus()
                == ClassSessionStatus.CANCELLED) {

            throw new InvalidResourceException(
                    "Class session is already cancelled"
            );
        }


        classSession.setStatus(
                ClassSessionStatus.CANCELLED
        );


        ClassSession updated =
                classSessionRepository
                        .save(classSession);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // GET BY COURSE OFFERING
    // =====================================================

    public List<ClassSessionResponseDto>
    getByCourseOffering(
            Long courseOfferingId
    ) {

        checkCourseOfferingExists(
                courseOfferingId
        );


        return classSessionRepository
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

    public List<ClassSessionResponseDto>
    getByCourseOfferingAndStatus(
            Long courseOfferingId,
            ClassSessionStatus status
    ) {

        checkCourseOfferingExists(
                courseOfferingId
        );


        return classSessionRepository
                .findByCourseOffering_CourseOfferingIdAndStatus(
                        courseOfferingId,
                        status
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY STATUS
    // =====================================================

    public List<ClassSessionResponseDto>
    getByStatus(
            ClassSessionStatus status
    ) {

        return classSessionRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY DATE
    // =====================================================

    public List<ClassSessionResponseDto>
    getByDate(
            LocalDate sessionDate
    ) {

        return classSessionRepository
                .findBySessionDate(
                        sessionDate
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET TEACHER CLASS SESSIONS
    // =====================================================

    public List<ClassSessionResponseDto>
    getByTeacher(
            Long teacherId
    ) {

        return classSessionRepository
                .findByCourseOffering_Teacher_TeacherId(
                        teacherId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // PRIVATE FIND
    // =====================================================

    private ClassSession findClassSessionById(
            Long classSessionId
    ) {

        return classSessionRepository
                .findById(classSessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class session not found with id: "
                                        + classSessionId
                        )
                );
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

    private ClassSessionResponseDto mapToResponseDto(
            ClassSession classSession
    ) {

        CourseOffering offering =
                classSession
                        .getCourseOffering();


        ProgramCourse programCourse =
                offering
                        .getProgramCourse();


        Course course =
                programCourse
                        .getCourses();


        AcademicSemster semester =
                offering
                        .getAcademicSemester();


        Teacher teacher =
                offering
                        .getTeacher();


        Section section =
                offering
                        .getSection();


        return ClassSessionResponseDto.builder()

                .classSessionId(
                        classSession
                                .getClassSessionId()
                )

                .courseOfferingId(
                        offering
                                .getCourseOfferingId()
                )

                .courseId(
                        course
                                .getId()
                )

                .courseCode(
                        course
                                .getCourseCode()
                )

                .courseName(
                        course
                                .getCourseName()
                )

                .academicSemesterId(
                        semester
                                .getAcademicSemsterId()
                )

                .academicSemesterName(
                        semester
                                .getSemesterName()
                )

                .teacherId(
                        teacher
                                .getTeacherId()
                )

                .teacherName(
                        teacher.getTeacherFirstName()
                                + " "
                                + teacher.getTeacherLastName()
                )

                .sectionId(
                        section
                                .getSectionId()
                )

                .sectionCode(
                        section
                                .getSectionCode()
                )

                .sessionDate(
                        classSession
                                .getSessionDate()
                )

                .startTime(
                        classSession
                                .getStartTime()
                )

                .endTime(
                        classSession
                                .getEndTime()
                )

                .topic(
                        classSession
                                .getTopic()
                )

                .status(
                        classSession
                                .getStatus()
                )

                .build();
    }
}