package com.example.courseenrollmentsystem.ExceptionHandling;

public class DublicateNotAllowedException extends RuntimeException {
    public DublicateNotAllowedException(String message) {
        super(message);
    }
}
