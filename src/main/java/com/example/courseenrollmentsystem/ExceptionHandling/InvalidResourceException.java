package com.example.courseenrollmentsystem.ExceptionHandling;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(String message) {
        super(message);
    }
}
