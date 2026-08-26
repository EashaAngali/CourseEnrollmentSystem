package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.AttendanceDto.AttendanceResponseDto;
import com.example.courseenrollmentsystem.DTO.AttendanceDto.CreateAttendanceRequestDto;
import com.example.courseenrollmentsystem.Entity.Attendance;
import com.example.courseenrollmentsystem.Entity.ClassSession;
import com.example.courseenrollmentsystem.Entity.Enrollment;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.AttendanceRepository;
import com.example.courseenrollmentsystem.Repository.ClassSessionRepository;
import com.example.courseenrollmentsystem.Repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository classSessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    public AttendanceResponseDto markAttendance(
            CreateAttendanceRequestDto createAttendanceRequestDto) {
              ClassSession classSession = classSessionRepository.
                      findById(
                              createAttendanceRequestDto.
                                      getClassSessionId()).orElseThrow(
                        () -> new ResourceNotFoundException("Class session id not found")
                );
           Optional<Enrollment> enrollment= enrollmentRepository.
                   findByStudentSemester_Student_StudentCard_QrToken(
                           createAttendanceRequestDto.getQrToken());

           boolean isExist = enrollmentRepository.existsByStudentSemester_Student_StudentIdAndCourseOffering_CourseOfferingId(
                   enrollment.get().getStudentSemester().getStudent().getStudentId(),
                   classSession.getCourseOffering().getCourseOfferingId()
           );
           if(!isExist){
                throw new InvalidResourceException("Student not Enrolled in this course");
           }

                return null;
         }
}
