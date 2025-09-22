package org.example;

public enum CourseLevel {
    BEGINNER("Beginner", 80),
    INTERMEDIATE("Intermediate", 60),
    ADVANCE("Advance", 40),
    OPEN("OPEN", 100);

    private final String label;
    private final int defaultLimit;

    CourseLevel(String label, int defaultLimit) {
        this.label = label;
        this.defaultLimit = defaultLimit;
    }

    public String getLabel() {
        return label;
    }

    public int getDefaultLimit() {
        return defaultLimit;
    }
}
