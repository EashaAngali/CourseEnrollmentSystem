package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
