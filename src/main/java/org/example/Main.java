package org.example;

import org.example.exceptions.CourseFullException;

import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Main {
    public static void main(String[] args) {

        DanceSchool danceSchool = DanceSchool.getInstance();

        danceSchool.addStudentFromFile("Students");
        for (Student student: danceSchool.getStudents()) {
            System.out.println(student);
        }















        /*
        DanceSchool danceSchool = DanceSchool.getInstance();
        Course c1 = new Course("Bachata", "Basic", 1);
        danceSchool.addCourse(c1);

        Student s1 = new Student("Gienek", "Loska");
        Student s2 = new Student("Zbigniew", "Fiodor");
        danceSchool.addStudent(s1);
        danceSchool.addStudent(s2);

        try {
            danceSchool.studentToTheCourse(s1, "Bachata");
        } catch (CourseFullException e) {
            System.out.println(e);
        }
        try {
            danceSchool.studentToTheCourse(s2, "Bachata");
        } catch (CourseFullException e) {
            System.out.println(e);
        }
        */






    }
}