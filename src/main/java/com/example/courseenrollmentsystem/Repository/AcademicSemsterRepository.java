package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.AcademicSemster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AcademicSemsterRepository extends JpaRepository<AcademicSemster, Long> {
    Optional<AcademicSemster> findBysemesterName(String semesterName);
    Optional<AcademicSemster> findBystartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);
    Optional<AcademicSemster> findByenrollmentStartDateLessThanEqualAndEnrollmentEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

}
