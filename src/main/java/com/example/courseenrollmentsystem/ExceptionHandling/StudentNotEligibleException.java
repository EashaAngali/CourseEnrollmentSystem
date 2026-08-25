package com.example.courseenrollmentsystem.ExceptionHandling;

public class StudentNotEligibleException extends RuntimeException {
    public StudentNotEligibleException(String message) {
        super(message);
    }
}
