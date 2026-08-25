package com.example.courseenrollmentsystem.Repository;
import com.example.courseenrollmentsystem.Entity.ProgramCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface programCourseRepository extends JpaRepository<ProgramCourse, Long> {
    boolean existsByPrograms_programIdAndCourses_Id(
            Long ProgramId,
            Long courseId
    );

}
