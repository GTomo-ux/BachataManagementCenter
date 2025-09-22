package org.example;

public interface CourseFactory {
    Course create (String name, CourseLevel level);
    Course create (String name, CourseLevel level, int limit);
}
