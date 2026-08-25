package com.example.courseenrollmentsystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    @NotNull(message = "Please Enter Description Name")
    private String departmentName;
    @NotNull(message = "Please Enter Description Code")
    private String departmentCode;
    @NotNull(message = "Please Enter Description Description")
    private String departmentDescription;
    @NotNull(message = "Please Enter Description Status")
    private String departmentStatus;
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updateDate;


    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Program> program = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Course> course = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Teacher> teacher = new ArrayList<>();
}
