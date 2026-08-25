package com.example.courseenrollmentsystem.DTO.StudentCardDto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateStudentCardRequestDto {

    @NotNull(message = "Student id is required")
    private Long studentId;


    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;
}
