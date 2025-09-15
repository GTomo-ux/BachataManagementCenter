package org.example;

import static org.example.Room.ROOM1;
import static org.junit.jupiter.api.Assertions.*;

import org.example.exceptions.CourseFullException;
import org.example.exceptions.NotEnrolledException;
import org.example.exceptions.ScheduleConflictException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DanceSchoolTest {

    @Test
    public void testAddStudent () {
        DanceSchool danceSchool = DanceSchool.getInstance();
        Student student = new Student("Marcin", "Pierzchała");
        danceSchool.addStudent(student);
        assertTrue(danceSchool.getStudents().contains(student), "Student powinien być dodany do szkoły");
    }

    @Test
    public void testStudentToTheCourse_ThrowsExceptionWhenFull () {
        DanceSchool danceSchool = DanceSchool.getInstance();
        Course c1 = new Course("Bachata", "Basic", 1, null);
        danceSchool.addCourse(c1);



        Student s1 = new Student("Gienek", "Loska");
        Student s2 = new Student("Zbigniew", "Fiodor");
        danceSchool.addStudent(s1);
        danceSchool.addStudent(s2);

        assertDoesNotThrow(() -> danceSchool.studentToTheCourse(s1, "Bachata"));

        assertThrows(CourseFullException.class, () -> danceSchool.studentToTheCourse(s2, "Bachata"), "Powinno rzucić wyjątek przy próbie zapisania nad limit");

    }
    @Test
    public void testAddLesson_ScheduleConflictException () {
        DanceSchool danceSchool = DanceSchool.getInstance();
        Lesson l1 = new Lesson(
                LocalDateTime.of(2025,10,14,18,00),
                Duration.ofMinutes(90),
                Room.ROOM1
        );
        Lesson l2 = new Lesson(
                LocalDateTime.of(2025,10,14,18,30),
                Duration.ofMinutes(90),
                Room.ROOM1
        );
        Lesson l3 = new Lesson(
                LocalDateTime.of(2025,10,14,18,30),
                Duration.ofMinutes(90),
                Room.ROOM2
        );
        assertDoesNotThrow(() -> danceSchool.addLesson(l1));
        assertDoesNotThrow(() -> danceSchool.addLesson(l3));
        assertThrows(ScheduleConflictException.class, () -> danceSchool.addLesson(l2), "Powinno rzucić wyjątek, że jest kolizja lekcji!");

    }
    @Test
    public void removeStudentFromCourse_NotEnrolledException () {
        DanceSchool danceSchool = DanceSchool.getInstance();
        Course course = new Course("Salsa", "Basic", 20, null);
        Student student = new Student("Tomasz", "Kornik");
        Student student1 = new Student("Angelika", "Adamczyk");
        danceSchool.addCourse(course);
        danceSchool.addStudent(student);
        danceSchool.addStudent(student1);
        try {
            danceSchool.studentToTheCourse(student, "Salsa");
        } catch (CourseFullException e) {
            throw new RuntimeException(e);
        }
        assertDoesNotThrow(() -> danceSchool.removeStudentFromCourse(student, "Salsa"));
        assertThrows(NotEnrolledException.class, () -> danceSchool.removeStudentFromCourse(student1, "Salsa"));

    }
}
