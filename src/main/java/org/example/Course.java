package org.example;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private String level;
    private Instructor instructor;
    private int limitOfPlaces;
    private List <Student> students;
    private List <Lesson> lessons;

    public Course (String name, String level, Instructor instructor, int limitOfPlaces) {
        this.name = name;
        this.level = level;
        this.instructor = instructor;
        this.limitOfPlaces = limitOfPlaces;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    public Course (String name, String level, int limitOfPlaces) {
        this.name = name;
        this.level = level;
        this.instructor = null;
        this.limitOfPlaces = limitOfPlaces;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    public String getName() {
        return this.name;
    }
    public String getLevel() {
        return this.level;
    }
    public Instructor getInstructor() {
        return this.instructor;
    }
    public int getLimitOfPlaces() {
        return this.limitOfPlaces;
    }
    public List <Student> getStudents() {
        return this.students;
    }
    public List <Lesson> getLessons() {
        return this.lessons;
    }
    public void addStudent (Student student) {
        this.students.add(student);
    }
    public void addLesson (Lesson lesson) {
        this.lessons.add(lesson);
    }
    public void setInstructor (Instructor instructor) {
        this.instructor = instructor;
    }
    @Override
    public String toString () {
        return this.name + ", " + this.level + ", limit miejsc: " + this.limitOfPlaces;
    }
}
