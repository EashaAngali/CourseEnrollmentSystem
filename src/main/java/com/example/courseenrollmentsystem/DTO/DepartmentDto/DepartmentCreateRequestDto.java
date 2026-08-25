package com.example.courseenrollmentsystem.DTO.DepartmentDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class DepartmentCreateRequestDto {
    private Long Departmentid;
    private String Departmentname;
    private String Departmentcode;
    private String Departmentdescription;
    private String Departmentstatus;

}
