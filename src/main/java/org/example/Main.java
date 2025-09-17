package org.example;

import org.example.exceptions.CourseFullException;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        DanceSchool ds = DanceSchool.loadData();

        Student s1 = new Student("Michał", "Bieniek");
        Student s2 = new Student("Ewa", "Jancz");

        Instructor i1 = new Instructor("Claudio", "Mineo", 20);

        Course c1 = new Course("Bachata Fusion", "Intermediate", 60, i1);
        Course c2 = new Course("Bachata", "Basic", 80, i1);

        ds.addStudent(s1);
        ds.addStudent(s2);
        ds.addInstructor(i1);
        ds.addCourse(c1);
        ds.addCourse(c2);

        System.out.println("Studenci:");
        for (Student s : ds.getStudents()) {
            System.out.println(s);
        }

        System.out.println("\nKursy:");
        for (Course c : ds.getCourses()) {
            System.out.println(c);
        }

        ds.saveData();

        System.out.println("\n✅ Dane zapisane!");








    }
}