package com.example.courseenrollmentsystem.Enum.StudentEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum StudentSpringOrFallStatus {
    SPRING('S'),
    FALL('F');

    private final char code;
//
//    StudentSpringOrFallStatus(char code) {
//        this.code = code;
//    }
//
//    public char getCode() {
//        return code;
//    }
}
