package com.example.courseenrollmentsystem.Controller;
import com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO.AcademicSemsterResponseDto;
import com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO.AcadmeicSemsterRequestDto;
import com.example.courseenrollmentsystem.Enum.AcadmeicSemsterEnum.AcademicSemsterStatus;
import com.example.courseenrollmentsystem.Service.Interface.AcademicSemsterInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academicSemster")
public class AcademicSemsterController {
    private final AcademicSemsterInterface academicSemsterInterface;
    @PostMapping
    public ResponseEntity<AcademicSemsterResponseDto> createAcademicSemster(@RequestBody AcadmeicSemsterRequestDto academicSemsterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicSemsterInterface.createAcademicSemster(academicSemsterRequestDto));
    }
    @GetMapping
    public ResponseEntity<List<AcademicSemsterResponseDto>> getAcademicSemster(){
        return ResponseEntity.ok(academicSemsterInterface.fetchAllAcademicSemsterdata());
    }
    @GetMapping("/{id}")
    public ResponseEntity<AcademicSemsterResponseDto> getAcademicSemsterByID(@PathVariable Long id){
        return ResponseEntity.ok(academicSemsterInterface.fetchAllAcademicSemsterdataById(id));
    }
    @GetMapping("/cuurent")
    public ResponseEntity<List<AcademicSemsterResponseDto>> getAcademicSemsterByCurrent(){
        return ResponseEntity.ok(academicSemsterInterface.getAcademicSemsterByCurrent());
    }
    @GetMapping("/search")
    public ResponseEntity<List<AcademicSemsterResponseDto>> getAcademicSemsterByStatus(@RequestBody AcademicSemsterStatus status){
        return ResponseEntity.ok(academicSemsterInterface.getAcademicSemsterByStatus(status));
    }
    @GetMapping("/enrollment-open")
    public ResponseEntity<List<AcademicSemsterResponseDto>> getEnrollmentOpen(){
        return ResponseEntity.ok(academicSemsterInterface.fetchEnrollmentOpen());
    }
    @PatchMapping("{id}")
    public ResponseEntity<AcademicSemsterResponseDto> updateAcademicSemster(@PathVariable Long id,@RequestBody Map<String, Object> map) {
        return ResponseEntity.ok(academicSemsterInterface.updateAcademicSesmter(id,map));

    }
}
