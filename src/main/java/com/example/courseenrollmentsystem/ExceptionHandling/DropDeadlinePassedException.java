package com.example.courseenrollmentsystem.ExceptionHandling;

public class DropDeadlinePassedException extends RuntimeException {
    public DropDeadlinePassedException(String message) {
        super(message);
    }
}
