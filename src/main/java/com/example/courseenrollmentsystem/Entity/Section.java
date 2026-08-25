package com.example.courseenrollmentsystem.Entity;

import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"program_id", "semester_number", "section_name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false, unique = true)
    private String sectionCode;

    @Column(nullable = false)
    private Integer semesterNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;
}