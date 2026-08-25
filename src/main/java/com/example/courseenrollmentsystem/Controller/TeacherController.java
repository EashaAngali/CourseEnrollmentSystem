package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.TeacherDto.TeacherRequestDto;
import com.example.courseenrollmentsystem.DTO.TeacherDto.TeacherResponseDto;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import com.example.courseenrollmentsystem.Service.Interface.TeacherInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherInterface teacherInterface;

    @PostMapping
    public ResponseEntity<TeacherResponseDto> addTeacher(@RequestBody TeacherRequestDto teacherRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherInterface.saveTeacher(teacherRequestDto));
    }
    @GetMapping
    public ResponseEntity<List<TeacherResponseDto>> getAllTeacher() {
        return ResponseEntity.ok(teacherInterface.fetchAllTeachersData());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherInterface.fetchTeacherById(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> updateTeacherByCatagory(@PathVariable Long id, @RequestBody Map<String, Object> map) {
        return ResponseEntity.ok(teacherInterface.upadteTeacher(id,map));
    }
    @GetMapping("/departments/{id}")
    public ResponseEntity<List<TeacherResponseDto>> getTeacherByDepartmentId(@PathVariable Long id){
            return ResponseEntity.ok(teacherInterface.fetchTeacherBydepartmentId(id));
    }
    @GetMapping("/status")
    public ResponseEntity<List<TeacherResponseDto>> getTeacherByStatus(TeacherStatus status){
        return ResponseEntity.ok(teacherInterface.fetchTeacherByStatus(status));
    }
    @GetMapping("/department/{id}/teachers")
    public ResponseEntity<List<TeacherResponseDto>> getTeacherByDepAndStatus(@PathVariable Long id, TeacherStatus status){
        return ResponseEntity.ok(teacherInterface.fetchTeacherByDepAndStatus(id,status));
    }
    @GetMapping("/name")
    public ResponseEntity<List<TeacherResponseDto>> searchTeacherByName(String name){
        return ResponseEntity.ok(teacherInterface.fetchTeacherByName(name));
    }
    @GetMapping("/email")
    public ResponseEntity<List<TeacherResponseDto>> searchTeacherByEmail(String email){
        return ResponseEntity.ok(teacherInterface.fetchTeacherByEmail(email));
    }
    @GetMapping("/employeeCode/{id}")
    public ResponseEntity<TeacherResponseDto> getTeacherByEmployeeCode(@PathVariable String id){
        return ResponseEntity.ok(teacherInterface.getTeacherByEmployeeCode(id));
    }
    @GetMapping("/list")
    public ResponseEntity<List<TeacherResponseDto>> getAllTeacherByPages(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                                                         @RequestParam(required = false,defaultValue = "10") int pageSize,
                                                                         @RequestParam(required = false, defaultValue = "Teacher_id") String sortBy,
                                                                         @RequestParam(required = false, defaultValue = "ASC") String sortOrder){
        Sort sort = null;
        if(sortOrder.equals("ASC")){
           sort= Sort.by(sortBy).ascending();
        }else{
           sort= Sort.by(sortBy).descending();
        }
        PageRequest pageRequest = PageRequest.of(pageNo-1, pageSize,sort);
        return ResponseEntity.ok(teacherInterface.page(pageRequest));
    }
}
