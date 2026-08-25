package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.CourseOffreingDto.CourseOfferingResponseDto;
import com.example.courseenrollmentsystem.DTO.CourseOffreingDto.CreateCourseOfferingRequestDto;
import com.example.courseenrollmentsystem.DTO.CourseOffreingDto.UpdateCourseOfferingRequestDto;
import com.example.courseenrollmentsystem.Entity.*;
import com.example.courseenrollmentsystem.Enum.AcadmeicSemsterEnum.AcademicSemsterStatus;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.ProgramCourseStatus;
import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseOfferingService {

    private final CourseOfferingRepository courseOfferingRepository;
    private final programCourseRepository programCourseRepository;
    private final AcademicSemsterRepository academicSemesterRepository;
    private final TeacherRepository teacherRepository;
    private final SectionRepository sectionRepository;


    // =====================================================
    // CREATE COURSE OFFERING
    // =====================================================

    @Transactional
    public CourseOfferingResponseDto createCourseOffering(
            CreateCourseOfferingRequestDto request
    ) {

        ProgramCourse programCourse =
                programCourseRepository
                        .findById(request.getProgramCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Program course not found with id: "
                                                + request.getProgramCourseId()
                                )
                        );


        AcademicSemster academicSemester =
                academicSemesterRepository
                        .findById(request.getAcademicSemesterId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic semester not found with id: "
                                                + request.getAcademicSemesterId()
                                )
                        );


        Teacher teacher =
                teacherRepository
                        .findById(request.getTeacherId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + request.getTeacherId()
                                )
                        );


        Section section =
                sectionRepository
                        .findById(request.getSectionId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Section not found with id: "
                                                + request.getSectionId()
                                )
                        );


        // ---------------------------------------------
        // CHECK PROGRAM COURSE STATUS
        // ---------------------------------------------

        if (programCourse.getStatus()
                != ProgramCourseStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Inactive program course cannot be offered"
            );
        }


        // ---------------------------------------------
        // CHECK TEACHER STATUS
        // ---------------------------------------------

        if (teacher.getTeacherStatus()
                != TeacherStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Only active teachers can be assigned"
            );
        }


        // ---------------------------------------------
        // CHECK SECTION STATUS
        // ---------------------------------------------

        if (section.getStatus()
                != SectionStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Inactive section cannot be used"
            );
        }


        // ---------------------------------------------
        // CHECK ACADEMIC SEMESTER
        // ---------------------------------------------

        if (academicSemester.getAcademicSemsterStatus()
                == AcademicSemsterStatus.COMPLETED) {

            throw new InvalidResourceException(
                    "Course offering cannot be created in a completed semester"
            );
        }


        // ---------------------------------------------
        // PROGRAM MATCHING
        // ProgramCourse Program == Section Program
        // ---------------------------------------------

        Long programCourseProgramId =
                programCourse
                        .getPrograms()
                        .getProgramId();

        Long sectionProgramId =
                section
                        .getProgram()
                        .getProgramId();


        if (!programCourseProgramId.equals(sectionProgramId)) {

            throw new InvalidResourceException(
                    "Program course and section belong to different programs"
            );
        }


        // ---------------------------------------------
        // SEMESTER NUMBER MATCHING
        // ---------------------------------------------

        if (!programCourse
                .getRecommendedSemester()
                .equals(section.getSemesterNumber())) {

            throw new InvalidResourceException(
                    "Program course semester and section semester do not match"
            );
        }


        // ---------------------------------------------
        // CAPACITY VALIDATION
        // ---------------------------------------------

        if (request.getCapacity() > section.getCapacity()) {

            throw new InvalidResourceException(
                    "Course offering capacity cannot be greater than section capacity"
            );
        }


        // ---------------------------------------------
        // DUPLICATE CHECK
        // ---------------------------------------------

        boolean alreadyExists =
                courseOfferingRepository
                        .existsByProgramCourse_ProgramCourseIdAndAcademicSemester_AcademicSemsterIdAndSection_SectionId(
                                request.getProgramCourseId(),
                                request.getAcademicSemesterId(),
                                request.getSectionId()
                        );


        if (alreadyExists) {

            throw new DublicateNotAllowedException(
                    "Course offering already exists for this course, semester and section"
            );
        }


        // ---------------------------------------------
        // CREATE ENTITY
        // ---------------------------------------------

        CourseOffering courseOffering =
                CourseOffering.builder()

                        .programCourse(programCourse)

                        .academicSemester(
                                academicSemester
                        )

                        .teacher(teacher)

                        .section(section)

                        .capacity(
                                request.getCapacity()
                        )

                        .status(
                                CourseOfferingStatus.DRAFT
                        )

                        .build();


        CourseOffering savedCourseOffering =
                courseOfferingRepository
                        .save(courseOffering);


        return mapToResponseDto(savedCourseOffering);
    }


    // =====================================================
    // GET COURSE OFFERING BY ID
    // =====================================================

    public CourseOfferingResponseDto getCourseOfferingById(
            Long courseOfferingId
    ) {

        CourseOffering courseOffering =
                findCourseOfferingById(courseOfferingId);


        return mapToResponseDto(courseOffering);
    }


    // =====================================================
    // GET ALL COURSE OFFERINGS
    // =====================================================

    public List<CourseOfferingResponseDto> getAllCourseOfferings() {

        return courseOfferingRepository
                .findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // UPDATE COURSE OFFERING
    // Teacher + Capacity + Status
    // =====================================================

    @Transactional
    public CourseOfferingResponseDto updateCourseOffering(
            Long courseOfferingId,
            UpdateCourseOfferingRequestDto request
    ) {

        CourseOffering courseOffering =
                findCourseOfferingById(courseOfferingId);


        Teacher teacher =
                teacherRepository
                        .findById(request.getTeacherId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + request.getTeacherId()
                                )
                        );


        // Teacher must be active

        if (teacher.getTeacherStatus()
                != TeacherStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Only active teachers can be assigned"
            );
        }


        // Capacity cannot exceed Section capacity

        if (request.getCapacity()
                > courseOffering
                .getSection()
                .getCapacity()) {

            throw new InvalidResourceException(
                    "Course offering capacity cannot be greater than section capacity"
            );
        }


        courseOffering.setTeacher(teacher);

        courseOffering.setCapacity(
                request.getCapacity()
        );

        courseOffering.setStatus(
                request.getStatus()
        );


        CourseOffering updatedCourseOffering =
                courseOfferingRepository
                        .save(courseOffering);


        return mapToResponseDto(updatedCourseOffering);
    }


    // =====================================================
    // CHANGE STATUS
    // =====================================================

    @Transactional
    public CourseOfferingResponseDto changeStatus(
            Long courseOfferingId,
            CourseOfferingStatus status
    ) {

        CourseOffering courseOffering =
                findCourseOfferingById(courseOfferingId);


        courseOffering.setStatus(status);


        CourseOffering updatedCourseOffering =
                courseOfferingRepository
                        .save(courseOffering);


        return mapToResponseDto(updatedCourseOffering);
    }


    // =====================================================
    // GET BY ACADEMIC SEMESTER
    // =====================================================

    public List<CourseOfferingResponseDto> getByAcademicSemester(
            Long academicSemesterId
    ) {

        academicSemesterRepository
                .findById(academicSemesterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic semester not found with id: "
                                        + academicSemesterId
                        )
                );


        return courseOfferingRepository
                .findByAcademicSemester_AcademicSemsterId(
                        academicSemesterId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY TEACHER
    // =====================================================

    public List<CourseOfferingResponseDto> getByTeacher(
            Long teacherId
    ) {

        teacherRepository
                .findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher not found with id: "
                                        + teacherId
                        )
                );


        return courseOfferingRepository
                .findByTeacher_TeacherId(teacherId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY SECTION
    // =====================================================

    public List<CourseOfferingResponseDto> getBySection(
            Long sectionId
    ) {

        sectionRepository
                .findById(sectionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section not found with id: "
                                        + sectionId
                        )
                );


        return courseOfferingRepository
                .findBySection_SectionId(sectionId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY PROGRAM
    // =====================================================

    public List<CourseOfferingResponseDto> getByProgram(
            Long programId
    ) {

        return courseOfferingRepository
                .findByProgramCourse_Programs_programId(
                        programId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY STATUS
    // =====================================================

    public List<CourseOfferingResponseDto> getByStatus(
            CourseOfferingStatus status
    ) {

        return courseOfferingRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET BY ACADEMIC SEMESTER + STATUS
    // =====================================================

    public List<CourseOfferingResponseDto> getBySemesterAndStatus(
            Long academicSemesterId,
            CourseOfferingStatus status
    ) {

        academicSemesterRepository
                .findById(academicSemesterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic semester not found with id: "
                                        + academicSemesterId
                        )
                );


        return courseOfferingRepository
                .findByAcademicSemester_AcademicSemsterIdAndStatus(
                        academicSemesterId,
                        status
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // GET TEACHER OFFERINGS BY SEMESTER
    // =====================================================

    public List<CourseOfferingResponseDto>
    getTeacherOfferingsBySemester(
            Long teacherId,
            Long academicSemesterId
    ) {

        teacherRepository
                .findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher not found with id: "
                                        + teacherId
                        )
                );


        academicSemesterRepository
                .findById(academicSemesterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic semester not found with id: "
                                        + academicSemesterId
                        )
                );


        return courseOfferingRepository
                .findByTeacher_TeacherIdAndAcademicSemester_AcademicSemsterId(
                        teacherId,
                        academicSemesterId
                )
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }


    // =====================================================
    // PRIVATE FIND METHOD
    // =====================================================

    private CourseOffering findCourseOfferingById(
            Long courseOfferingId
    ) {

        return courseOfferingRepository
                .findById(courseOfferingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course offering not found with id: "
                                        + courseOfferingId
                        )
                );
    }


    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    private CourseOfferingResponseDto mapToResponseDto(
            CourseOffering courseOffering
    ) {

        ProgramCourse programCourse =
                courseOffering.getProgramCourse();


        Teacher teacher =
                courseOffering.getTeacher();


        return CourseOfferingResponseDto.builder()

                .courseOfferingId(
                        courseOffering.getCourseOfferingId()
                )

                .programCourseId(
                        programCourse.getProgramCourseId()
                )

                .programId(
                        programCourse
                                .getPrograms()
                                .getProgramId()
                )

                .programName(
                        programCourse
                                .getPrograms()
                                .getProgramName()
                )

                .courseId(
                        programCourse
                                .getCourses()
                                .getId()
                )

                .courseCode(
                        programCourse
                                .getCourses()
                                .getCourseCode()
                )

                .courseName(
                        programCourse
                                .getCourses()
                                .getCourseName()
                )

                .recommendedSemester(
                        programCourse
                                .getRecommendedSemester()
                )

                .academicSemesterId(
                        courseOffering
                                .getAcademicSemester()
                                .getAcademicSemsterId()
                )

                .academicSemesterName(
                        courseOffering
                                .getAcademicSemester()
                                .getSemesterName()
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
                        courseOffering
                                .getSection()
                                .getSectionId()
                )

                .sectionCode(
                        courseOffering
                                .getSection()
                                .getSectionCode()
                )

                .capacity(
                        courseOffering.getCapacity()
                )

                .status(
                        courseOffering.getStatus()
                )

                .build();
    }
}
