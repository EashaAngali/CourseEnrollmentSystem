package com.example.courseenrollmentsystem.Entity;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceMethod;
import com.example.courseenrollmentsystem.Enum.AttendanceEnum.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
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
       @Column(nullable = false)
       private String classSession;
       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name="enrollment_id",
               nullable = false)
       private Enrollment enrollment;
       @OneToMany
       private List<Course> courses;
       @Enumerated(EnumType.STRING)
       @Column(nullable = false)
       private AttendanceStatus attendanceStatus;
       @Enumerated(EnumType.STRING)
       @Column(nullable = false)
       private AttendanceMethod attendanceMethod;
       @Column(nullable = false)
       private LocalDate attendanceDate;

}
