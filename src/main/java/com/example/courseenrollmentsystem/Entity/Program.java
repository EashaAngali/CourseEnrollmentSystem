package com.example.courseenrollmentsystem.Entity;
import com.example.courseenrollmentsystem.Enum.ProgramEnum.ProgramStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;
    @NotNull(message = "Please Enter Program Name")
    private String ProgramName;
    @NotNull(message = "Please Enter Program Code")
    private String ProgramCode;
    @NotNull(message = "Please Enter Duration")
    @Min(0)
    private Integer durationInYears;
    @NotNull(message = "Please Enter totalSemster")
    private Integer totalSemster;
    @NotNull(message = "Please Enter maximumCreditHoursPerSemester")
    private Integer maximumCreditHoursPerSemester;
    @NotNull(message = "Please Enter programStatus")
    @Enumerated(EnumType.STRING)
    private ProgramStatus programStatus;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    @OneToMany(mappedBy = "programs")
    @JsonIgnore
    private List<ProgramCourse> programCourse;

    @OneToMany(mappedBy = "program")
    @JsonIgnore
    private List<Section> section;
}
