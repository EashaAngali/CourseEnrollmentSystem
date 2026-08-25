package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO.AcademicSemsterResponseDto;
import com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO.AcadmeicSemsterRequestDto;
import com.example.courseenrollmentsystem.Enum.AcadmeicSemsterEnum.AcademicSemsterStatus;

import java.util.List;
import java.util.Map;

public interface AcademicSemsterInterface {
    AcademicSemsterResponseDto createAcademicSemster(AcadmeicSemsterRequestDto academicSemsterRequestDto);

    List<AcademicSemsterResponseDto> fetchAllAcademicSemsterdata();

    AcademicSemsterResponseDto fetchAllAcademicSemsterdataById(Long id);

    List<AcademicSemsterResponseDto> getAcademicSemsterByCurrent();

    List<AcademicSemsterResponseDto> getAcademicSemsterByStatus(AcademicSemsterStatus status);

    List<AcademicSemsterResponseDto> fetchEnrollmentOpen();


    AcademicSemsterResponseDto updateAcademicSesmter(Long id, Map<String, Object> map);
}
