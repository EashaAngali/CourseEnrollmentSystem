package com.example.courseenrollmentsystem.DTO.StudentSemsterDto;
import com.example.courseenrollmentsystem.Enum.StudentSemesterEnum.StudentSemesterStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentSemesterRequestDto {

    @NotNull(message = "Registration status is required")
    private StudentSemesterStatus registrationStatus;

    private Double semesterGpa;

    private Double cumulativeGpa;
}