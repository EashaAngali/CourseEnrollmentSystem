package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
  Optional<Student> findByEmail(String studentEmail);
  Optional<Student> findByCnic(String studentCNIC);
  Optional<Student> findTopByStudentNumberStartingWithOrderByStudentNumberDesc(String studentNumber);
//  Optional<Student> findProgram_programCodeByProgram_ProgramId(String programCode);

}
