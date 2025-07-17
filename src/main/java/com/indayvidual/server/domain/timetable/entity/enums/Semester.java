package com.indayvidual.server.domain.timetable.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Semester {
    FIRST_YEAR_FIRST("1학년 1학기"),
    FIRST_YEAR_SECOND("1학년 2학기"),
    SECOND_YEAR_FIRST("2학년 1학기"),
    SECOND_YEAR_SECOND("2학년 2학기"),
    THIRD_YEAR_FIRST("3학년 1학기"),
    THIRD_YEAR_SECOND("3학년 2학기"),
    FOURTH_YEAR_FIRST("4학년 1학기"),
    FOURTH_YEAR_SECOND("4학년 2학기");

    private final String label;

    Semester(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Semester from(String value) {
        return Arrays.stream(values())
                .filter(s -> s.label.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 학기입니다."));
    }
}

