package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.CourseDto.CourseCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.CourseDto.CourseUpdateRequestDto;
import com.example.courseenrollmentsystem.DTO.DepartmentDto.DepartmentCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.DepartmentDto.DepartmentUpdateRequestDto;
import com.example.courseenrollmentsystem.Service.Interface.DepartmentInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentInterface departmentInterface;
    @GetMapping
    public ResponseEntity<List<DepartmentCreateRequestDto>> findAll(){
        return ResponseEntity.status(HttpStatus.FOUND).body(departmentInterface.viewAllCourse());
    }
    @PostMapping()
    public ResponseEntity<DepartmentCreateRequestDto > addDepartment(@RequestBody DepartmentCreateRequestDto createRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentInterface.addDepartent(createRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentCreateRequestDto > getDepartmentById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(departmentInterface.getDepartmentbyId(id));
    }
    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id){
        return departmentInterface.deleteDepartment(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentUpdateRequestDto> updateDepartment(@PathVariable Long id , @RequestBody DepartmentUpdateRequestDto  departmentupdateRequestDto){
        return ResponseEntity.ok(departmentInterface.updateDepartment(id,departmentupdateRequestDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentUpdateRequestDto> updateDepartmentByCategory(@PathVariable Long id,@RequestBody Map<String, Object> map){
        return ResponseEntity.ok(departmentInterface.updateDepartmentByCatagory(id,map));
    }

}
