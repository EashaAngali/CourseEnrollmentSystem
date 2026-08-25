package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.ClassSessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "class_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classSessionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_offering_id",
            nullable = false
    )
    private CourseOffering courseOffering;


    @Column(nullable = false)
    private LocalDate sessionDate;


    @Column(nullable = false)
    private LocalTime startTime;


    @Column(nullable = false)
    private LocalTime endTime;


    private String topic;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassSessionStatus status;
}
