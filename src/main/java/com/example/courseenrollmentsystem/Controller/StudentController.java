package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.StudentDto.StudentCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentResponseDto;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Student;
import com.example.courseenrollmentsystem.Service.Interface.Studentinterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final Studentinterface studentinterface;
    @PostMapping()
    public ResponseEntity<StudentResponseDto> addStudent(@RequestBody StudentCreateRequestDto studentCreateRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(studentinterface.addstudent(studentCreateRequestDto));
    }
    @GetMapping()
    public ResponseEntity<List<StudentResponseDto>> getAllStudents(){
        return ResponseEntity.status(HttpStatus.FOUND).body(studentinterface.getAllStudent());
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(studentinterface.getStudentbyId(id));
    }
    @DeleteMapping("/{id}")
    public String deleteStudentById(@PathVariable Long id){
        return studentinterface.deleteStudent(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id , @RequestBody StudentUpdateRequestDto studentUpdateRequestDto){
        return ResponseEntity.ok(studentinterface.updateStudent(id, studentUpdateRequestDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudentbycatagory(@PathVariable Long id, @RequestBody Map<String, Object> map){
        return ResponseEntity.ok(studentinterface.updateStudentByCatagory(id,map));
    }
}
