package com.example.courseenrollmentsystem.Entity;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceMethod;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long attendanceId;
       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(
               name = "class_session_id",
               nullable = false
       )
       private ClassSession classSession;
       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name="enrollment_id",
               nullable = false)
       private Enrollment enrollment;
       @Enumerated(EnumType.STRING)
       @Column(nullable = false)
       private AttendanceStatus attendanceStatus;
       @Enumerated(EnumType.STRING)
       @Column(nullable = false)
       private AttendanceMethod attendanceMethod;
       @Column(nullable = false)
       private LocalDateTime attendanceDate;

}
