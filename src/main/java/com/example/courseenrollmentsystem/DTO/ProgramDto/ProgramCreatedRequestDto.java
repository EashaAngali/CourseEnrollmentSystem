package com.example.courseenrollmentsystem.DTO.ProgramDto;

import com.example.courseenrollmentsystem.Entity.Department;
import com.example.courseenrollmentsystem.Enum.ProgramEnum.ProgramStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ProgramCreatedRequestDto {
    private Long ProgramId;
    private String ProgramName;
    private String ProgramCode;
    private Integer durationInYears;
    private Integer totalSemster;
    private Integer maximumCreditHoursPerSemester;
    private ProgramStatus programStatus;
    private Long Department_Id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
