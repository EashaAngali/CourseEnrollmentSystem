package com.example.courseenrollmentsystem.Service.services;
import com.example.courseenrollmentsystem.DTO.ProgramDto.ProgramCreatedRequestDto;
import com.example.courseenrollmentsystem.DTO.ProgramDto.ProgramUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Department;
import com.example.courseenrollmentsystem.Entity.Program;
import com.example.courseenrollmentsystem.Enum.ProgramEnum.ProgramStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.DepartmentRepository;
import com.example.courseenrollmentsystem.Repository.ProgramRepository;
import com.example.courseenrollmentsystem.Service.Interface.ProgramInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgramService implements ProgramInterface {
    private final ProgramRepository programRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<ProgramCreatedRequestDto> viewAllProgram() {
        List<Program> programs = programRepository.findAll();
        return programs.stream().map(
                program -> modelMapper.map(
                        program, ProgramCreatedRequestDto.class)).toList();
    }

    @Override
    public ProgramCreatedRequestDto addProgram(ProgramCreatedRequestDto createRequestDto) {
        Program program = modelMapper.map(createRequestDto, Program.class);
        Department department= departmentRepository.findById(createRequestDto.getDepartment_Id()).orElseThrow(
                ()-> new ResourceNotFoundException("Department Not Found"));
        program.setDepartment(department);
        Program createdProgram = programRepository.save(program);
        ProgramCreatedRequestDto responseDto = modelMapper.map(createdProgram, ProgramCreatedRequestDto.class);
        responseDto.setDepartment_Id(createdProgram.getDepartment().getDepartmentId());
        return responseDto;
    }

    @Override
    public ProgramCreatedRequestDto getProgrambyId(Long id) {
        Program program = findProgramById(id);
        return modelMapper.map(program, ProgramCreatedRequestDto.class);
    }

    @Override
    public String deleteProgram(Long id) {
        Program program = findProgramById(id);
        programRepository.delete(program);
        return "Program Deleted Successfully";
    }

    @Override
    public ProgramUpdateRequestDto updateProgram(Long id, ProgramUpdateRequestDto courseupdateRequestDto) {
        Program program = findProgramById(id);
        modelMapper.map(courseupdateRequestDto,program);
        program = programRepository.save(program);
        return modelMapper.map(program, ProgramUpdateRequestDto.class);
    }

    @Override
    public ProgramUpdateRequestDto updateProgramByCatagory(Long id, Map<String, Object> map) {
        Program program = findProgramById(id);
        map.forEach((fieldName, fieldValue) -> {
            switch (fieldName) {
                case "ProgramName":
                    program.setProgramName(fieldValue.toString());
                    break;
                case "ProgramCode":
                    program.setProgramCode(fieldValue.toString());
                    break;
                case "durationInYears":
                    program.setDurationInYears(Integer.parseInt(fieldValue.toString()));
                    break;
                case "totalSemster":
                    program.setTotalSemster(Integer.parseInt(fieldValue.toString()));
                    break;
                case "maximumCreditHoursPerSemester":
                    program.setMaximumCreditHoursPerSemester(Integer.parseInt(fieldValue.toString()));
                    break;
                case "programStatus":

                        program.setProgramStatus(ProgramStatus.valueOf(fieldValue.toString()));
            }
        });
        Program updatedProgram = programRepository.save(program);
        return modelMapper.map(updatedProgram, ProgramUpdateRequestDto.class);
    }

    public Program findProgramById(Long id) {
        return programRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Program Not Found")
        );
    }
}
