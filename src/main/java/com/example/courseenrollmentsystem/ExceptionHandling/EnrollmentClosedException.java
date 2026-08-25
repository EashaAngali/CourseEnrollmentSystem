package com.example.courseenrollmentsystem.ExceptionHandling;

public class EnrollmentClosedException extends RuntimeException {
    public EnrollmentClosedException(String message) {
        super(message);
    }
}
