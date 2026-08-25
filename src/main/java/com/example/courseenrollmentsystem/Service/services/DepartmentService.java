package com.example.courseenrollmentsystem.Service.services;
import com.example.courseenrollmentsystem.DTO.DepartmentDto.DepartmentCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.DepartmentDto.DepartmentUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Department;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.DepartmentRepository;
import com.example.courseenrollmentsystem.Service.Interface.DepartmentInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentInterface {
    private final DepartmentRepository  departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DepartmentCreateRequestDto> viewAllCourse() {
        List<Department> department = departmentRepository.findAll();
        return department.stream().map(department1 -> modelMapper.map(department1,DepartmentCreateRequestDto.class)).toList();
    }

    @Override
    public DepartmentCreateRequestDto addDepartent(DepartmentCreateRequestDto createRequestDto) {
        Department department = modelMapper.map(createRequestDto, Department.class);
        if(departmentRepository.findByDepartmentCode(createRequestDto.getDepartmentcode()).isPresent()){
            throw new DublicateNotAllowedException("DepartmentCode Already Exists");
        }
        Department newDepartment = departmentRepository.save(department);
        return modelMapper.map(newDepartment,DepartmentCreateRequestDto.class);
    }

    @Override
    public DepartmentCreateRequestDto getDepartmentbyId(Long id) {
        Department department = findByID(id);
        return modelMapper.map(department,DepartmentCreateRequestDto.class);
    }

    @Override
    public String deleteDepartment(Long id) {
        Department department = findByID(id);
        departmentRepository.delete(department);
        return "Department has been deleted";
    }

    @Override
    public DepartmentUpdateRequestDto updateDepartment(Long id, DepartmentUpdateRequestDto departmentupdateRequestDto) {
        Department department = findByID(id);
        modelMapper.map(departmentupdateRequestDto, department);
        department =  departmentRepository.save(department);
        return modelMapper.map(department,DepartmentUpdateRequestDto.class);
    }
    @Override
    public DepartmentUpdateRequestDto updateDepartmentByCatagory(Long id, Map<String, Object> map) {
        Department department = findByID(id);
        map.forEach((fieldName,fieldValue)->{
            switch (fieldName){
                case "DepartmentName":
                    department.setDepartmentName(fieldValue.toString());
                    break;
                case "DepartmentCode":
                    department.setDepartmentCode(fieldValue.toString());
                    break;
                case "DepartmentDescription":
                    department.setDepartmentDescription(fieldValue.toString());
                    break;
                case "DepartmentStatus":
                    department.setDepartmentStatus(fieldValue.toString());
                    break;
                default:
                    System.out.println("Enter Valid Field");
                    break;
            }
        });
        Department newDepartment = departmentRepository.save(department);
        return modelMapper.map(newDepartment,DepartmentUpdateRequestDto.class);
    }
    public Department findByID(Long id) {
        return departmentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Department with id " + id + " not found")
        );
    }
}
