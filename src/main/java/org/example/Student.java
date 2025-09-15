package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private String name;
    private String surname;
    private int ID;
    private List <Course> courses;

    public Student (String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.courses = new ArrayList<>();
    }
    public String getName() {
        return this.name;
    }
    public String getSurname() {
        return this.surname;
    }
    public void setID (int ID) {
        this.ID = ID;
    }
    public int getID() {
        return this.ID;
    }
    public List <Course> getCourses() {
        return this.courses;
    }
    public void add (Course course) {
        this.courses.add(course);
    }
    public void remove (Course course) {
        this.courses.remove(course);
    }
    @Override
    public String toString () {
        return this.name + " " + this.surname + ", ID: " + this.ID;
    }


}
