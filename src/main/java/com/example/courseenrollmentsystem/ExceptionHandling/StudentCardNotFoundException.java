package com.example.courseenrollmentsystem.ExceptionHandling;

public class StudentCardNotFoundException extends RuntimeException {
    public StudentCardNotFoundException(String message) {
        super(message);
    }
}
