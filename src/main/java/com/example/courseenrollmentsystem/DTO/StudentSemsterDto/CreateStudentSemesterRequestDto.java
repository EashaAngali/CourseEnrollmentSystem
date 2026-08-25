package com.example.courseenrollmentsystem.DTO.StudentSemsterDto;


    import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class CreateStudentSemesterRequestDto {

        @NotNull(message = "Student id is required")
        private Long studentId;


        @NotNull(message = "Academic semester id is required")
        private Long academicSemesterId;


        @NotNull(message = "Semester number is required")
        @Positive(message = "Semester number must be positive")
        private Integer semesterNumber;
    }

