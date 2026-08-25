package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    Optional<Teacher> findByTeacherEmail(String email);
    Optional<Teacher> findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(String prefix);
    List<Teacher> findByDepartment_DepartmentId(Long id);
    Optional<Teacher> findByEmployeeCode(String employeeCode);
}
