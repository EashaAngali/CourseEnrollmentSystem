package com.example.courseenrollmentsystem.DTO.DepartmentDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DepartmentUpdateRequestDto {
    private String DepartmentName;
    private String DepartmentCode;
    private String DepartmentDescription;
    private String DepartmentStatus;
}
