package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {


}
