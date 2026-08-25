package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

}
