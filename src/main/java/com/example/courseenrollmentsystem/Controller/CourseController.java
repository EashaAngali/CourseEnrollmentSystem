package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.CourseDto.CourseCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.CourseDto.CourseUpdateRequestDto;
import com.example.courseenrollmentsystem.Service.Interface.CourseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faculty/course")
public class CourseController {
    private final CourseInterface courseInterface;
    @GetMapping
    public ResponseEntity<List<CourseCreateRequestDto>> findAll(){
        return ResponseEntity.status(HttpStatus.FOUND).body(courseInterface.viewAllCourse());
    }
    @PostMapping()
    public ResponseEntity<CourseCreateRequestDto> addCourse(@RequestBody CourseCreateRequestDto createRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(courseInterface.addCourse(createRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseCreateRequestDto> getCourseById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(courseInterface.getCoursebyId(id));
    }
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id){
        return courseInterface.deleteCourse(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CourseUpdateRequestDto> updateCourse(@PathVariable Long id , @RequestBody CourseUpdateRequestDto  courseupdateRequestDto){
        return ResponseEntity.ok(courseInterface.updateCourse(id,courseupdateRequestDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<CourseUpdateRequestDto> updateCoursebycatagory(@PathVariable Long id,@RequestBody Map<String, Object> map){
        return ResponseEntity.ok(courseInterface.updateCourseByCatagory(id,map));
    }
}
