package com.example.courseenrollmentsystem.ExceptionHandling;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
}
