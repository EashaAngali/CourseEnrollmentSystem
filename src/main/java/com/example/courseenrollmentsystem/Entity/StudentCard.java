package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.StudentCard.StudentCardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "student_cards",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_card_number",
                        columnNames = "card_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentCardId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    private Student student;


    @Column(
            name = "card_number",
            nullable = false,
            unique = true
    )
    private String cardNumber;


    @Column(nullable = false)
    private LocalDate issueDate;


    @Column(nullable = false)
    private LocalDate expiryDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentCardStatus status;
    @Column(nullable = false, unique = true)
    private String qrToken;
}