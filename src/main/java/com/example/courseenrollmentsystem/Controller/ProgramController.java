package com.example.courseenrollmentsystem.Controller;
import com.example.courseenrollmentsystem.DTO.ProgramDto.ProgramCreatedRequestDto;
import com.example.courseenrollmentsystem.DTO.ProgramDto.ProgramUpdateRequestDto;
import com.example.courseenrollmentsystem.Service.Interface.ProgramInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program")
public class ProgramController {
    private final ProgramInterface programInterface;
    @GetMapping
    public ResponseEntity<List<ProgramCreatedRequestDto>> findAll(){
        return ResponseEntity.status(HttpStatus.FOUND).body(programInterface.viewAllProgram());
    }
    @PostMapping()
    public ResponseEntity<ProgramCreatedRequestDto> addProgram(@RequestBody ProgramCreatedRequestDto createRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(programInterface.addProgram(createRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramCreatedRequestDto> getProgramById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(programInterface.getProgrambyId(id));
    }
    @DeleteMapping("/{id}")
    public String deleteProgram(@PathVariable Long id){
        return programInterface.deleteProgram(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProgramUpdateRequestDto> updateProgram(@PathVariable Long id , @RequestBody ProgramUpdateRequestDto  courseupdateRequestDto){
        return ResponseEntity.ok(programInterface.updateProgram(id,courseupdateRequestDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProgramUpdateRequestDto> updateProgrambycatagory(@PathVariable Long id,@RequestBody Map<String, Object> map){
        return ResponseEntity.ok(programInterface.updateProgramByCatagory(id,map));
    }
}
