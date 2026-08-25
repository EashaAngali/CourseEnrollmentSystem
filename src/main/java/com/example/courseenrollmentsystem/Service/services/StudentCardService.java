package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.StudentCardDto.CreateStudentCardRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentCardDto.StudentCardResponseDto;
import com.example.courseenrollmentsystem.Entity.Program;
import com.example.courseenrollmentsystem.Entity.Student;
import com.example.courseenrollmentsystem.Entity.StudentCard;
import com.example.courseenrollmentsystem.Enum.StudentCard.StudentCardStatus;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.ExceptionHandling.StudentAlreadyHasActiveCardException;
import com.example.courseenrollmentsystem.Repository.StudentCardRepository;
import com.example.courseenrollmentsystem.Repository.StudentRepository;
import com.example.courseenrollmentsystem.qrGenerator;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StudentCardService {


    private final StudentCardRepository studentCardRepository;

    private final StudentRepository studentRepository;


    // =====================================================
    // CREATE / ISSUE STUDENT CARD
    // =====================================================

    @Transactional
    public StudentCardResponseDto createStudentCard(
            CreateStudentCardRequestDto request
    ) {

        // ---------------------------------------------
        // FIND STUDENT
        // ---------------------------------------------

        Student student =
                studentRepository
                        .findById(request.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + request.getStudentId()
                                )
                        );


        // ---------------------------------------------
        // STUDENT MUST BE ACTIVE
        // ---------------------------------------------

        if (student.getStatus()
                != StudentStatus.ACTIVE) {

            throw new InvalidResourceException(
                    "Student card can only be issued to an active student"
            );
        }


        // ---------------------------------------------
        // CHECK EXISTING ACTIVE CARD
        //
        // Ek student ka ek waqt mein sirf
        // one ACTIVE card hona chahiye.
        // ---------------------------------------------

        boolean activeCardExists =
                studentCardRepository
                        .existsByStudent_StudentIdAndStatus(
                                student.getStudentId(),
                                StudentCardStatus.ACTIVE
                        );


        if (activeCardExists) {

            throw new StudentAlreadyHasActiveCardException(
                    "Student already has an active card"
            );
        }


        LocalDate today = LocalDate.now();


        // ---------------------------------------------
        // EXPIRY DATE VALIDATION
        // ---------------------------------------------

        if (!request.getExpiryDate().isAfter(today)) {

            throw new InvalidResourceException(
                    "Expiry date must be after issue date"
            );
        }


        // ---------------------------------------------
        // AUTO GENERATE CARD NUMBER
        //
        // Example:
        // Student Number = BSCS-2026-001
        //
        // Card Number =
        // CARD-BSCS-2026-001
        // ---------------------------------------------

        String cardNumber =
                generateCardNumber(student);


        // Safety duplicate check

        if (studentCardRepository
                .existsByCardNumber(cardNumber)) {

            throw new InvalidResourceException(
                    "Generated card number already exists"
            );
        }


        // ---------------------------------------------
        // CREATE CARD
        // ---------------------------------------------

        StudentCard studentCard =
                StudentCard.builder()

                        .student(student)

                        .cardNumber(cardNumber)

                        .issueDate(today)

                        .expiryDate(
                                request.getExpiryDate()
                        )

                        .status(
                                StudentCardStatus.ACTIVE
                        )

                        .qrToken(generateQrToken())

                        .build();


        StudentCard savedCard =
                studentCardRepository
                        .save(studentCard);


        return mapToResponseDto(savedCard);
    }


    // =====================================================
    // GET CARD BY ID
    // =====================================================

    public StudentCardResponseDto getStudentCardById(
            Long studentCardId
    ) {

        StudentCard studentCard =
                findStudentCardById(studentCardId);


        // Check expiry before returning
        updateExpiryStatusIfRequired(studentCard);


        return mapToResponseDto(studentCard);
    }


    // =====================================================
    // GET ALL CARDS
    // =====================================================

    public List<StudentCardResponseDto>
    getAllStudentCards() {

        return studentCardRepository
                .findAll()
                .stream()
                .map(card -> {

                    updateExpiryStatusIfRequired(card);

                    return mapToResponseDto(card);
                })
                .toList();
    }


    // =====================================================
    // GET ALL CARDS OF ONE STUDENT
    //
    // Future mein old + replacement history bhi
    // isi method se milegi.
    // =====================================================

    public List<StudentCardResponseDto>
    getCardsByStudent(
            Long studentId
    ) {

        checkStudentExists(studentId);


        return studentCardRepository
                .findByStudent_StudentId(studentId)
                .stream()
                .map(card -> {

                    updateExpiryStatusIfRequired(card);

                    return mapToResponseDto(card);
                })
                .toList();
    }


    // =====================================================
    // GET ACTIVE CARD OF STUDENT
    // =====================================================

    public StudentCardResponseDto getActiveCardByStudent(
            Long studentId
    ) {

        checkStudentExists(studentId);


        StudentCard card =
                studentCardRepository
                        .findByStudent_StudentIdAndStatus(
                                studentId,
                                StudentCardStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active student card not found for student id: "
                                                + studentId
                                )
                        );


        // Card database mein ACTIVE ho sakta hai
        // but date actually expire ho chuki ho.
        updateExpiryStatusIfRequired(card);


        if (card.getStatus()
                != StudentCardStatus.ACTIVE) {

            throw new ResourceNotFoundException(
                    "Student does not have an active card"
            );
        }


        return mapToResponseDto(card);
    }


    // =====================================================
    // GET CARD BY CARD NUMBER
    // =====================================================

    public StudentCardResponseDto getByCardNumber(
            String cardNumber
    ) {

        StudentCard card =
                studentCardRepository
                        .findByCardNumber(cardNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student card not found with card number: "
                                                + cardNumber
                                )
                        );


        updateExpiryStatusIfRequired(card);


        return mapToResponseDto(card);
    }


    // =====================================================
    // BLOCK CARD
    //
    // Lost / stolen card
    // =====================================================

    @Transactional
    public StudentCardResponseDto blockCard(
            Long studentCardId
    ) {

        StudentCard studentCard =
                findStudentCardById(studentCardId);


        updateExpiryStatusIfRequired(studentCard);


        // ---------------------------------------------
        // EXPIRED CARD BLOCK NAHI KARENGE
        // ---------------------------------------------

        if (studentCard.getStatus()
                == StudentCardStatus.EXPIRED) {

            throw new InvalidResourceException(
                    "Expired student card cannot be blocked"
            );
        }


        // ---------------------------------------------
        // ALREADY BLOCKED
        // ---------------------------------------------

        if (studentCard.getStatus()
                == StudentCardStatus.BLOCKED) {

            throw new InvalidResourceException(
                    "Student card is already blocked"
            );
        }


        studentCard.setStatus(
                StudentCardStatus.BLOCKED
        );


        StudentCard updatedCard =
                studentCardRepository
                        .save(studentCard);


        return mapToResponseDto(updatedCard);
    }


    // =====================================================
    // PRIVATE - FIND CARD
    // =====================================================

    private StudentCard findStudentCardById(
            Long studentCardId
    ) {

        return studentCardRepository
                .findById(studentCardId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student card not found with id: "
                                        + studentCardId
                        )
                );
    }


    // =====================================================
    // PRIVATE - CHECK STUDENT
    // =====================================================

    private void checkStudentExists(
            Long studentId
    ) {

        if (!studentRepository
                .existsById(studentId)) {

            throw new ResourceNotFoundException(
                    "Student not found with id: "
                            + studentId
            );
        }
    }


    // =====================================================
    // PRIVATE - AUTO GENERATE CARD NUMBER
    //
    // Student number:
    // BSCS-2026-001
    //
    // Card number:
    // CARD-BSCS-2026-001
    // =====================================================

    private String generateCardNumber(
            Student student
    ) {

        return "CARD-"
                + student
                .getStudentNumber()
                .toUpperCase();
    }


    // =====================================================
    // PRIVATE - CHECK EXPIRY
    // =====================================================

    private void updateExpiryStatusIfRequired(
            StudentCard studentCard
    ) {

        LocalDate today = LocalDate.now();


        if (studentCard.getStatus()
                == StudentCardStatus.ACTIVE

                && today.isAfter(
                studentCard.getExpiryDate()
        )) {


            studentCard.setStatus(
                    StudentCardStatus.EXPIRED
            );


            studentCardRepository
                    .save(studentCard);
        }
    }


    // =====================================================
    // PRIVATE - RESPONSE DTO
    // =====================================================

    private StudentCardResponseDto mapToResponseDto(
            StudentCard studentCard
    ) {

        Student student =
                studentCard.getStudent();


        Program program =
                student.getProgram();


        return StudentCardResponseDto.builder()

                .studentCardId(
                        studentCard.getStudentCardId()
                )

                .studentId(
                        student.getStudentId()
                )

                .studentNumber(
                        student.getStudentNumber()
                )

                .studentName(
                        student.getFirstName()
                                + " "
                                + student.getLastName()
                )

                .programId(
                        program.getProgramId()
                )

                .programName(
                        program.getProgramName()
                )

                .cardNumber(
                        studentCard.getCardNumber()
                )

                .issueDate(
                        studentCard.getIssueDate()
                )

                .expiryDate(
                        studentCard.getExpiryDate()
                )

                .status(
                        studentCard.getStatus()
                )
                .qrToken(generateQrToken())
                .build();
    }

    private String generateQrToken(){
        String token;
        do {
            token = UUID.randomUUID().toString();
        }
        while (studentCardRepository.existsByQrToken(token));
        return token;
    }

    public byte[] getStudentCardQr(
            Long studentCardId
    ){
        StudentCard studentCard = studentCardRepository
                .findById(studentCardId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Student card not found:"
                        )
                );
        if(studentCard.getStatus() != StudentCardStatus.ACTIVE){
            throw new InvalidResourceException(
                    "Only active student cards can be blocked");
        }

        LocalDate today = LocalDate.now();


        // ---------------------------------------------
        // EXPIRY DATE VALIDATION
        // ---------------------------------------------

        if (!studentCard.getExpiryDate().isAfter(today)) {

            throw new InvalidResourceException(
                    "generate QR code are not allowed to for expired student cards "
            );
        }
//        boolean activeCardExists =
//                studentCardRepository
//                        .existsByStatus(
//                                StudentCardStatus.ACTIVE
//                        );
//        if(!activeCardExists){
//            throw new StudentAlreadyHasActiveCardException("Student card has been blocked");
//        }
        if(studentCard.getQrToken() == null){
            throw new InvalidResourceException("qrToken is null");
        }
        qrGenerator generator = new qrGenerator();

        return generator.generateQrCode(
                studentCard.getQrToken(),
                300,
                300
        );
    }
}