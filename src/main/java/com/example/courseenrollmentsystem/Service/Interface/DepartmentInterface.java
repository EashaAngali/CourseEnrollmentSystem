package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.DepartmentDto.DepartmentCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.DepartmentDto.DepartmentUpdateRequestDto;

import java.util.List;
import java.util.Map;

public interface DepartmentInterface {
    List<DepartmentCreateRequestDto> viewAllCourse();

    DepartmentCreateRequestDto addDepartent(DepartmentCreateRequestDto createRequestDto);

    DepartmentCreateRequestDto getDepartmentbyId(Long id);

    String deleteDepartment(Long id);

    DepartmentUpdateRequestDto updateDepartment(Long id, DepartmentUpdateRequestDto departmentupdateRequestDto);

    DepartmentUpdateRequestDto updateDepartmentByCatagory(Long id, Map<String, Object> map);
}
