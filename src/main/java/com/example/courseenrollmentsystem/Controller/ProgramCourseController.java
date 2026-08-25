package com.example.courseenrollmentsystem.Controller;

import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCResponseDto;
import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCcreateRequestDto;
import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCupdateRequestDto;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.CourseType;
import com.example.courseenrollmentsystem.Service.Interface.ProgramCourseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/programCourse")
public class ProgramCourseController {
    private final ProgramCourseInterface programCourseInterface;

    @PostMapping
    public ResponseEntity<PCResponseDto> createProgramCourse(@RequestBody PCcreateRequestDto pccreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programCourseInterface.addprogramcourse(pccreateRequestDto));
    }
    @GetMapping
    public ResponseEntity<List<PCResponseDto>> getAllProgramCourse() {
        return ResponseEntity.ok(programCourseInterface.getAllProgramCourse());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PCResponseDto> getProgramCourse(@PathVariable Long id) {
        return ResponseEntity.ok(programCourseInterface.getProgramCourseById(id));
    }
    @GetMapping(value = "/programs/{programId}/courses",params = "!courseType")
    public ResponseEntity<List<PCResponseDto>> getAllProgramCourseByProgramId(@PathVariable Long programId) {
        return ResponseEntity.ok(programCourseInterface.getCourseByProgramId(programId));
    }
    @GetMapping("/programs/{programId}/semesters/{semesterNumber}/courses")
    public ResponseEntity<List<PCResponseDto>> getCourseByProgramIdAndSemesterNumber(@PathVariable Long programId, @PathVariable Integer semesterNumber) {
        return ResponseEntity.ok(programCourseInterface.getCourseByProgramIdAndSemster(programId,semesterNumber));
    }
    @GetMapping(value = "/programs/{programId}/courses/search",params = "courseType")
    public ResponseEntity<List<PCResponseDto>> getCourseByCourseType(@PathVariable Long programId,@RequestParam CourseType courseType) {
        return ResponseEntity.ok(programCourseInterface.getCourseByCourseType(programId,courseType));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<PCResponseDto>  updateProgramCourse(@PathVariable Long id, @RequestBody Map<String,Object> updateRequestDto) {
        return ResponseEntity.ok(programCourseInterface.updateprogramcourse(id,updateRequestDto));
    }
}
