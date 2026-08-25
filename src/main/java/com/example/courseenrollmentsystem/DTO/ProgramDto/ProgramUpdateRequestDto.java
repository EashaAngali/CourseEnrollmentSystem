package com.example.courseenrollmentsystem.DTO.ProgramDto;

import com.example.courseenrollmentsystem.Enum.ProgramEnum.ProgramStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProgramUpdateRequestDto {
    private String ProgramName;
    private String ProgramCode;
    private Integer durationInYears;
    private Integer totalSemster;
    private Integer maximumCreditHoursPerSemester;
    private ProgramStatus programStatus;
}
