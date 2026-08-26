package com.example.courseenrollmentsystem.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler({EnrollmentClosedException.class,
            CourseOfferingFullException.class,
            StudentNotEligibleException.class,
            DropDeadlinePassedException.class,
            ResourceNotFoundException.class,
            InvalidResourceException.class,
            StudentCardNotFoundException.class,
            AttendanceAlreadyMarkedException.class,
            StudentAlreadyHasActiveCardException.class,
            DublicateNotAllowedException.class,
            InvalidDateException.class})
    public ResponseEntity<Map<String,Object>> handleResourceNotFount(Exception ex){
        Map<String,Object> map = new HashMap<>();
        map.put("Status", HttpStatus.NOT_FOUND.value());
        map.put("message", ex.getMessage());
        map.put("timestamp", LocalDateTime.now());
        map.put("error", "Request Error");
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

}
