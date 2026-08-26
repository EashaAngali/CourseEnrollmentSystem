package com.example.courseenrollmentsystem.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

//Course Name
//Description
//        Category
//Duration
//        Fee
//Status
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Please Enter Course Name")
    private String courseName;
    @NotNull(message = "Please Enter Course Description")
    private String courseDescription;
    @NotNull(message = "Please Enter Course Code")
    private String courseCode;
    @NotNull(message = "please Enter Course Category")
    private String courseCategory;
    @NotNull(message = "Please Enter Duration")
    private String duration;
    @NotNull(message = "please Enter Course Fee")
    @Min(1)
    private String courseFee;
    @NotNull(message = "Please Enter Course Status ")
    private String courseStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "courses")
    @JsonIgnore
    private List<ProgramCourse> programCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;
}
