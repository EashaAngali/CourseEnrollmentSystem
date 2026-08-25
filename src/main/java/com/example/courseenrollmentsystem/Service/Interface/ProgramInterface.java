package com.example.courseenrollmentsystem.Service.Interface;

import com.example.courseenrollmentsystem.DTO.ProgramDto.ProgramCreatedRequestDto;
import com.example.courseenrollmentsystem.DTO.ProgramDto.ProgramUpdateRequestDto;

import java.util.List;
import java.util.Map;

public interface ProgramInterface {
    List<ProgramCreatedRequestDto> viewAllProgram();

    ProgramCreatedRequestDto addProgram(ProgramCreatedRequestDto createRequestDto);

    ProgramCreatedRequestDto getProgrambyId(Long id);

    String deleteProgram(Long id);

    ProgramUpdateRequestDto updateProgram(Long id, ProgramUpdateRequestDto courseupdateRequestDto);

    ProgramUpdateRequestDto updateProgramByCatagory(Long id, Map<String, Object> map);
}
