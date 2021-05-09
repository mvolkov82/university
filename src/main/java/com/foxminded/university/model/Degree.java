package com.foxminded.university.model;

public enum Degree {
    TEACHER("Teacher"),
    LECTURER("Lecturer"),
    PROFESSOR("Professor"),
    ACADEMIC("Academic");

    private final String displayValue;

    Degree(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
