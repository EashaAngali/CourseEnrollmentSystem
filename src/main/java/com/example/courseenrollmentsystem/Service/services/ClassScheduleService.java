package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.ClassSchedule.ClassScheduleResponseDto;
import com.example.courseenrollmentsystem.DTO.ClassSchedule.CreateClassScheduleRequestDto;
import com.example.courseenrollmentsystem.DTO.ClassSchedule.UpdateClassScheduleRequestDto;
import com.example.courseenrollmentsystem.DTO.ClassSessionDto.ClassSessionResponseDto;
import com.example.courseenrollmentsystem.Entity.*;
import com.example.courseenrollmentsystem.Enum.ClassSchedule.ClassScheduleStatus;
import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.ClassScheduleRepository;
import com.example.courseenrollmentsystem.Repository.ClassSessionRepository;
import com.example.courseenrollmentsystem.Repository.CourseOfferingRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClassScheduleService {


    private final ClassScheduleRepository classScheduleRepository;

    private final CourseOfferingRepository courseOfferingRepository;

    private final ClassSessionRepository classSessionRepository;


    // =====================================================
    // CREATE CLASS SCHEDULE
    // =====================================================

    @Transactional
    public ClassScheduleResponseDto createClassSchedule(
            CreateClassScheduleRequestDto request
    ) {

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
                    "Cannot create schedule for cancelled course offering"
            );
        }


        if (courseOffering.getStatus()
                == CourseOfferingStatus.COMPLETED) {

            throw new InvalidResourceException(
                    "Cannot create schedule for completed course offering"
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
        // DUPLICATE VALIDATION
        // ---------------------------------------------

        boolean alreadyExists =
                classScheduleRepository
                        .existsByCourseOffering_CourseOfferingIdAndDayOfWeekAndStartTime(
                                request.getCourseOfferingId(),
                                request.getDayOfWeek(),
                                request.getStartTime()
                        );


        if (alreadyExists) {

            throw new InvalidResourceException(
                    "Schedule already exists for this course offering, day and start time"
            );
        }


        ClassSchedule classSchedule =
                ClassSchedule.builder()

                        .courseOffering(courseOffering)

                        .dayOfWeek(
                                request.getDayOfWeek()
                        )

                        .startTime(
                                request.getStartTime()
                        )

                        .endTime(
                                request.getEndTime()
                        )

                        .room(
                                request.getRoom()
                        )

                        .status(
                                ClassScheduleStatus.ACTIVE
                        )

                        .build();


        ClassSchedule saved =
                classScheduleRepository
                        .save(classSchedule);


        return mapToResponseDto(saved);
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public ClassScheduleResponseDto getById(
            Long classScheduleId
    ) {

        return mapToResponseDto(
                findClassScheduleById(
                        classScheduleId
                )
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<ClassScheduleResponseDto>
    getAllClassSchedules() {

        return classScheduleRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY COURSE OFFERING
    // =====================================================

    public List<ClassScheduleResponseDto>
    getByCourseOffering(
            Long courseOfferingId
    ) {

        if (!courseOfferingRepository
                .existsById(courseOfferingId)) {

            throw new ResourceNotFoundException(
                    "Course offering not found with id: "
                            + courseOfferingId
            );
        }


        return classScheduleRepository
                .findByCourseOffering_CourseOfferingId(
                        courseOfferingId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public ClassScheduleResponseDto updateClassSchedule(
            Long classScheduleId,
            UpdateClassScheduleRequestDto request
    ) {

        ClassSchedule schedule =
                findClassScheduleById(
                        classScheduleId
                );


        // ---------------------------------------------
        // DON'T CHANGE RULE AFTER SESSIONS GENERATED
        // ---------------------------------------------

        boolean sessionsAlreadyGenerated =
                classSessionRepository
                        .existsByClassSchedule_ClassScheduleId(
                                classScheduleId
                        );


        if (sessionsAlreadyGenerated) {

            throw new InvalidResourceException(
                    "Schedule cannot be changed because class sessions have already been generated"
            );
        }


        if (schedule.getStatus()
                != ClassScheduleStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Only active schedule can be updated"
            );
        }


        if (!request.getEndTime()
                .isAfter(request.getStartTime())) {

            throw new InvalidResourceException(
                    "End time must be after start time"
            );
        }


        boolean duplicate =
                classScheduleRepository
                        .existsByCourseOffering_CourseOfferingIdAndDayOfWeekAndStartTime(
                                schedule
                                        .getCourseOffering()
                                        .getCourseOfferingId(),

                                request.getDayOfWeek(),

                                request.getStartTime()
                        );


        /*
         * NOTE:
         * Is simple version mein agar same schedule ka day/time
         * unchanged ho to duplicate true aa sakta hai.
         *
         * Better version later repository mein
         * "...AndClassScheduleIdNot" use kar sakti ho.
         */


        schedule.setDayOfWeek(
                request.getDayOfWeek()
        );

        schedule.setStartTime(
                request.getStartTime()
        );

        schedule.setEndTime(
                request.getEndTime()
        );

        schedule.setRoom(
                request.getRoom()
        );


        ClassSchedule updated =
                classScheduleRepository
                        .save(schedule);


        return mapToResponseDto(updated);
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @Transactional
    public ClassScheduleResponseDto activateSchedule(
            Long classScheduleId
    ) {

        ClassSchedule schedule =
                findClassScheduleById(
                        classScheduleId
                );


        if (schedule.getStatus()
                == ClassScheduleStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Class schedule is already active"
            );
        }


        schedule.setStatus(
                ClassScheduleStatus.ACTIVE
        );


        return mapToResponseDto(
                classScheduleRepository.save(schedule)
        );
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @Transactional
    public ClassScheduleResponseDto deactivateSchedule(
            Long classScheduleId
    ) {

        ClassSchedule schedule =
                findClassScheduleById(
                        classScheduleId
                );


        if (schedule.getStatus()
                == ClassScheduleStatus.INACTIVE) {

            throw new InvalidResourceException(
                    "Class schedule is already inactive"
            );
        }


        schedule.setStatus(
                ClassScheduleStatus.INACTIVE
        );


        return mapToResponseDto(
                classScheduleRepository.save(schedule)
        );
    }


    // =====================================================
    // GENERATE ALL CLASS SESSIONS
    // =====================================================

    @Transactional
    public List<ClassSessionResponseDto> generateSessions(
            Long classScheduleId
    ) {

        // ---------------------------------------------
        // 1. FIND SCHEDULE
        // ---------------------------------------------

        ClassSchedule schedule =
                findClassScheduleById(
                        classScheduleId
                );


        // ---------------------------------------------
        // 2. SCHEDULE MUST BE ACTIVE
        // ---------------------------------------------

        if (schedule.getStatus()
                != ClassScheduleStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Cannot generate sessions for inactive schedule"
            );
        }


        // ---------------------------------------------
        // 3. GET COURSE OFFERING
        // ---------------------------------------------

        CourseOffering offering =
                schedule.getCourseOffering();


        // ---------------------------------------------
        // 4. GET ACADEMIC SEMESTER
        // ---------------------------------------------

        AcademicSemster semester =
                offering.getAcademicSemester();


        LocalDate semesterStartDate =
                semester.getStartDate();


        LocalDate semesterEndDate =
                semester.getEndDate();


        // ---------------------------------------------
        // 5. FIND FIRST MATCHING DAY
        //
        // Example:
        // Semester starts Tuesday
        // Schedule = Thursday
        //
        // nextOrSame(THURSDAY)
        // -> first Thursday
        // ---------------------------------------------

        LocalDate currentDate =
                semesterStartDate.with(
                        TemporalAdjusters.nextOrSame(
                                schedule.getDayOfWeek()
                        )
                );


        List<ClassSessionResponseDto> generatedSessions =
                new ArrayList<>();


        // ---------------------------------------------
        // 6. LOOP UNTIL SEMESTER END
        // ---------------------------------------------

        while (!currentDate.isAfter(
                semesterEndDate
        )) {


            // -----------------------------------------
            // 7. CHECK DUPLICATE
            // -----------------------------------------

            boolean alreadyExists =
                    classSessionRepository
                            .existsByClassSchedule_ClassScheduleIdAndSessionDate(
                                    schedule.getClassScheduleId(),
                                    currentDate
                            );


            // -----------------------------------------
            // 8. CREATE ONLY IF NOT EXISTS
            // -----------------------------------------

            if (!alreadyExists) {

                ClassSession classSession =
                        ClassSession.builder()

                                .courseOffering(
                                        offering
                                )

                                .classSchedule(
                                        schedule
                                )

                                .sessionDate(
                                        currentDate
                                )

                                .startTime(
                                        schedule.getStartTime()
                                )

                                .endTime(
                                        schedule.getEndTime()
                                )

                                .topic(null)

                                .status(
                                        ClassSessionStatus.SCHEDULED
                                )

                                .build();


                ClassSession saved =
                        classSessionRepository
                                .save(classSession);


                generatedSessions.add(
                        mapSessionToResponseDto(
                                saved
                        )
                );
            }


            // -----------------------------------------
            // 9. MOVE EXACTLY ONE WEEK FORWARD
            // -----------------------------------------

            currentDate =
                    currentDate.plusWeeks(1);
        }


        return generatedSessions;
    }


    // =====================================================
    // GET GENERATED SESSIONS
    // =====================================================

    public List<ClassSessionResponseDto>
    getGeneratedSessions(
            Long classScheduleId
    ) {

        findClassScheduleById(
                classScheduleId
        );


        return classSessionRepository
                .findByClassSchedule_ClassScheduleId(
                        classScheduleId
                )
                .stream()
                .map(this::mapSessionToResponseDto)
                .toList();
    }


    // =====================================================
    // PRIVATE FIND
    // =====================================================

    private ClassSchedule findClassScheduleById(
            Long classScheduleId
    ) {

        return classScheduleRepository
                .findById(classScheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class schedule not found with id: "
                                        + classScheduleId
                        )
                );
    }


    // =====================================================
    // CLASS SCHEDULE -> RESPONSE
    // =====================================================

    private ClassScheduleResponseDto mapToResponseDto(
            ClassSchedule schedule
    ) {

        CourseOffering offering =
                schedule.getCourseOffering();

        ProgramCourse programCourse =
                offering.getProgramCourse();

        Course course =
                programCourse.getCourses();

        Teacher teacher =
                offering.getTeacher();

        Section section =
                offering.getSection();

        AcademicSemster semester =
                offering.getAcademicSemester();


        return ClassScheduleResponseDto.builder()

                .classScheduleId(
                        schedule.getClassScheduleId()
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

                .academicSemesterId(
                        semester.getAcademicSemsterId()
                )

                .academicSemesterName(
                        semester.getSemesterName()
                )

                .dayOfWeek(
                        schedule.getDayOfWeek()
                )

                .startTime(
                        schedule.getStartTime()
                )

                .endTime(
                        schedule.getEndTime()
                )

                .room(
                        schedule.getRoom()
                )

                .status(
                        schedule.getStatus()
                )

                .build();
    }


    // =====================================================
    // CLASS SESSION -> RESPONSE
    // =====================================================

    private ClassSessionResponseDto mapSessionToResponseDto(
            ClassSession classSession
    ) {

        CourseOffering offering =
                classSession.getCourseOffering();

        ProgramCourse programCourse =
                offering.getProgramCourse();

        Course course =
                programCourse.getCourses();

        Teacher teacher =
                offering.getTeacher();

        Section section =
                offering.getSection();

        AcademicSemster semester =
                offering.getAcademicSemester();


        return ClassSessionResponseDto.builder()

                .classSessionId(
                        classSession.getClassSessionId()
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
                                + teacher.getTeacherStatus()
                )

                .sectionId(
                        section.getSectionId()
                )

                .sectionCode(
                        section.getSectionCode()
                )

                .sessionDate(
                        classSession.getSessionDate()
                )

                .startTime(
                        classSession.getStartTime()
                )

                .endTime(
                        classSession.getEndTime()
                )

                .topic(
                        classSession.getTopic()
                )

                .status(
                        classSession.getStatus()
                )

                .build();
    }
}
