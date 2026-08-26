package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.Attendance;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    Optional<Attendance> findByAttendanceDateAndAttendanceStatus(LocalDate date, AttendanceStatus status);


}
