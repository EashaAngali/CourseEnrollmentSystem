package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.ClassSchedule.ClassScheduleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(
        name = "class_schedules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_schedule_offering_day_time",
                        columnNames = {
                                "course_offering_id",
                                "day_of_week",
                                "start_time"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classScheduleId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_offering_id",
            nullable = false
    )
    private CourseOffering courseOffering;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "day_of_week",
            nullable = false
    )
    private DayOfWeek dayOfWeek;


    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalTime startTime;


    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalTime endTime;


    private String room;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassScheduleStatus status;
}
