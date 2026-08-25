package com.example.courseenrollmentsystem.ExceptionHandling;

public class StudentAlreadyHasActiveCardException extends RuntimeException {
    public StudentAlreadyHasActiveCardException(String message) {
        super(message);
    }
}
