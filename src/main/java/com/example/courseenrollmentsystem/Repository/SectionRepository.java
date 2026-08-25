package com.example.courseenrollmentsystem.Repository;


import com.example.courseenrollmentsystem.Entity.Section;
import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface SectionRepository extends JpaRepository<Section, Long> {

        boolean existsByProgram_ProgramIdAndSemesterNumberAndSectionNameIgnoreCase(
                Long programId,
                Integer semesterNumber,
                String sectionName
        );

        List<Section> findByProgram_ProgramId(Long programId);

        List<Section> findByProgram_ProgramIdAndSemesterNumber(
                Long programId,
                Integer semesterNumber
        );

        List<Section> findByProgram_ProgramIdAndSemesterNumberAndStatus(
                Long programId,
                Integer semesterNumber,
                SectionStatus status
        );

        List<Section> findByStatus(SectionStatus status);
    }

