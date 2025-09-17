package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private String name;
    private String level;
    private Instructor instructor;
    private int limitOfPlaces;
    private List <Student> students;
    private List <Lesson> lessons;

    public Course (String name, String level, int limitOfPlaces, Instructor instructor) {
        this.name = name;
        this.level = level;
        this.limitOfPlaces = limitOfPlaces;
        this.instructor = instructor;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    public Course(String name, String level, int limitOfPlaces) {
        this(name, level, limitOfPlaces, null);
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
        return this.name + ", " + this.level + ", limit of places: " + this.limitOfPlaces;
    }
}
