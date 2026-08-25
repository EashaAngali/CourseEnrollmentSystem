package com.example.courseenrollmentsystem.DTO.EnrollmentDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEnrollmentRequestDto {

    @NotNull(message = "Student semester id is required")
    private Long studentSemesterId;
    @NotNull(message = "Course offering id is required")
    private Long courseOfferingId;
}