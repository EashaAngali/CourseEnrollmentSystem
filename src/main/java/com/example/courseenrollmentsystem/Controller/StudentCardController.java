package com.example.courseenrollmentsystem.Controller;
import com.example.courseenrollmentsystem.DTO.StudentCardDto.CreateStudentCardRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentCardDto.StudentCardResponseDto;
import com.example.courseenrollmentsystem.Service.services.StudentCardService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import java.util.List;


@RestController
@RequestMapping("/api/student-cards")
@RequiredArgsConstructor
public class StudentCardController {


    private final StudentCardService studentCardService;


    // =====================================================
    // CREATE / ISSUE CARD
    // =====================================================

    @PostMapping
    public ResponseEntity<StudentCardResponseDto>
    createStudentCard(

            @Valid
            @RequestBody
            CreateStudentCardRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        studentCardService
                                .createStudentCard(request)
                );
    }


    // =====================================================
    // GET ALL CARDS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<StudentCardResponseDto>>
    getAllStudentCards() {

        return ResponseEntity.ok(
                studentCardService
                        .getAllStudentCards()
        );
    }


    // =====================================================
    // GET CARD BY ID
    // =====================================================

    @GetMapping("/{studentCardId}")
    public ResponseEntity<StudentCardResponseDto>
    getStudentCardById(

            @PathVariable
            Long studentCardId
    ) {

        return ResponseEntity.ok(
                studentCardService
                        .getStudentCardById(
                                studentCardId
                        )
        );
    }


    // =====================================================
    // GET ALL CARDS OF STUDENT
    // =====================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentCardResponseDto>>
    getCardsByStudent(

            @PathVariable
            Long studentId
    ) {

        return ResponseEntity.ok(
                studentCardService
                        .getCardsByStudent(
                                studentId
                        )
        );
    }


    // =====================================================
    // GET STUDENT ACTIVE CARD
    // =====================================================

    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<StudentCardResponseDto>
    getActiveCardByStudent(

            @PathVariable
            Long studentId
    ) {

        return ResponseEntity.ok(
                studentCardService
                        .getActiveCardByStudent(
                                studentId
                        )
        );
    }


    // =====================================================
    // GET BY CARD NUMBER
    // =====================================================

    @GetMapping("/card-number/{cardNumber}")
    public ResponseEntity<StudentCardResponseDto>
    getByCardNumber(

            @PathVariable
            String cardNumber
    ) {

        return ResponseEntity.ok(
                studentCardService
                        .getByCardNumber(cardNumber)
        );
    }


    // =====================================================
    // BLOCK CARD
    // =====================================================

    @PatchMapping("/{studentCardId}/block")
    public ResponseEntity<StudentCardResponseDto>
    blockCard(

            @PathVariable
            Long studentCardId
    ) {

        return ResponseEntity.ok(
                studentCardService
                        .blockCard(
                                studentCardId
                        )
        );

    }
    @GetMapping(value = "/{studentCardId}/qr",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> hetStudentCardQr(
            @PathVariable Long studentCardId
    ){
        byte[] qrImage = studentCardService.
                getStudentCardQr(studentCardId);
        return ResponseEntity.ok(qrImage);
    }
    
}