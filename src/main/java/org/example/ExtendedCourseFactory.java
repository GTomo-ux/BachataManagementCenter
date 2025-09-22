package org.example;

public class ExtendedCourseFactory implements CourseFactory{

    @Override
    public Course create(String name, CourseLevel level) {
        return new Course(name, level.getLabel(), level.getDefaultLimit());
    }

    @Override
    public Course create(String name, CourseLevel level, int limit) {
        return new Course(name, level.getLabel(), limit);
    }
}
