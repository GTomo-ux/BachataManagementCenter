package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private String name;
    private String surname;
    private int ID = -1;
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
    void setID (int ID) {
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student that = (Student) o;
        return this.ID == that.ID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }


}
