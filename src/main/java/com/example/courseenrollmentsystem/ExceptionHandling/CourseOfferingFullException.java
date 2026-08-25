package com.example.courseenrollmentsystem.ExceptionHandling;

public class CourseOfferingFullException extends RuntimeException {
    public CourseOfferingFullException(String message) {
        super(message);
    }
}
