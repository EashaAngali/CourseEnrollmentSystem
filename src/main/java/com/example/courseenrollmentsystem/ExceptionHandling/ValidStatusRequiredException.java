package com.example.courseenrollmentsystem.ExceptionHandling;

public class ValidStatusRequiredException extends RuntimeException{
    public ValidStatusRequiredException(String message) {
        super(message);
    }
}
