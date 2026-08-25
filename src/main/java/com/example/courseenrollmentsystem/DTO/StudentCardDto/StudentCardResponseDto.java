package com.example.courseenrollmentsystem.DTO.StudentCardDto;

import com.example.courseenrollmentsystem.Enum.StudentCard.StudentCardStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCardResponseDto {

    private Long studentCardId;

    private Long studentId;

    private String studentNumber;

    private String studentName;

    private Long programId;

    private String programName;

    private String cardNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private StudentCardStatus status;
    private String qrToken;
}