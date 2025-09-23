package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Instructor implements Serializable {
    private String name;
    private String surname;
    private int experience;
    private List <Course> courses;

    public Instructor (String name, String surname, int experience) {
        this.name = name;
        this.surname = surname;
        this.experience = experience;
        this.courses = new ArrayList<>();
    }
    public String getName () {
        return this.name;
    }
    public String getSurname () {
        return this.surname;
    }
    public int getExperience () {
        return this.experience;
    }
    public List <Course> getCourses () {
        return this.courses;
    }
    public void add (Course course) {
        this.courses.add(course);
    }
    public void remove (Course course) {
        this.courses.remove(course);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Instructor)) {
            return false;
        }
        Instructor ins = (Instructor) obj;
        if (ins.getName().equals(this.name) &&
            ins.getSurname().equals(this.surname)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    @Override
    public String toString () {
        return this.name + " " + this.surname + ", " + this.experience + " years of experience.";
    }

}
