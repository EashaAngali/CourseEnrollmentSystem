package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.StudentCard;
import com.example.courseenrollmentsystem.Enum.StudentCard.StudentCardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentCardRepository
        extends JpaRepository<StudentCard, Long> {


    List<StudentCard> findByStudent_StudentId(
            Long studentId
    );


    Optional<StudentCard>
    findByStudent_StudentIdAndStatus(
            Long studentId,
            StudentCardStatus status
    );


    boolean existsByStudent_StudentIdAndStatus(
            Long studentId,
            StudentCardStatus status
    );
    boolean existsByStatus(
            StudentCardStatus status
    );


    boolean existsByCardNumber(
            String cardNumber
    );


    Optional<StudentCard> findByCardNumber(
            String cardNumber
    );
    boolean existsByQrToken(
            String qrToken
    );
    Optional<StudentCard> findByQrToken(String qrToken);
}