package org.example;

import org.example.exceptions.CourseFullException;
import org.example.exceptions.NotEnrolledException;
import org.example.exceptions.ScheduleConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DanceSchoolTest {

    private DanceSchoolService service;
    private DanceSchool school;

    @BeforeEach
    void setUp() {
        school = DanceSchool.getInstance();
        // wyczyszczenie singletona między testami (metoda pakietowa)
        school.clearAll();
        // nowy serwis (ustawi generator ID od zera)
        service = new DanceSchoolService();
    }

    @Test
    public void testAddStudent() {
        var student = service.addStudent("Marcin", "Pierzchała");
        assertTrue(school.getStudents().contains(student),
                "Student powinien być dodany do szkoły");
        assertTrue(student.getID() >= 0, "Student powinien otrzymać nadane ID");
    }

    @Test
    public void testStudentToTheCourse_ThrowsExceptionWhenFull() {
        var c1 = new Course("Bachata", "Basic", 1, null);
        service.addCourse(c1);

        var s1 = service.addStudent("Gienek", "Loska");
        var s2 = service.addStudent("Zbigniew", "Fiodor");

        assertDoesNotThrow(() -> service.studentToTheCourse(s1, "Bachata", "Beginner"),
                "Pierwszy zapis powinien przejść");

        assertThrows(CourseFullException.class,
                () -> service.studentToTheCourse(s2, "Bachata", "Beginner"),
                "Powinno rzucić wyjątek przy próbie zapisania nad limit");
    }

    @Test
    public void testAddLesson_ScheduleConflictException() {
        var l1 = new Lesson(
                LocalDateTime.of(2025, 10, 14, 18, 0),
                Duration.ofMinutes(90),
                Room.ROOM1
        );
        var l2 = new Lesson(
                LocalDateTime.of(2025, 10, 14, 18, 30),
                Duration.ofMinutes(90),
                Room.ROOM1
        );
        var l3 = new Lesson(
                LocalDateTime.of(2025, 10, 14, 18, 30),
                Duration.ofMinutes(90),
                Room.ROOM2
        );

        assertDoesNotThrow(() -> service.addLesson(l1));
        assertDoesNotThrow(() -> service.addLesson(l3));
        assertThrows(ScheduleConflictException.class,
                () -> service.addLesson(l2),
                "Powinno rzucić wyjątek, że jest kolizja lekcji!");
    }

    @Test
    public void removeStudentFromCourse_NotEnrolledException() {
        var course = new Course("Salsa", "Basic", 20, null);
        service.addCourse(course);

        var student = service.addStudent("Tomasz", "Kornik");
        var student1 = service.addStudent("Angelika", "Adamczyk");

        assertDoesNotThrow(() -> service.studentToTheCourse(student, "Salsa", "Beginner"),
                "Zapis powinien przejść");

        assertDoesNotThrow(() -> service.removeStudentFromCourse(student, "Salsa", "Beginner"),
                "Wypisanie zapisanego studenta powinno przejść");

        assertThrows(NotEnrolledException.class,
                () -> service.removeStudentFromCourse(student1, "Salsa", "Beginner"),
                "Powinno rzucić wyjątek dla studenta niezapisanego na kurs");
    }

    @Test
    public void InstructorEquality() {
        var a = new Instructor("Jan", "Nowak", 5);
        var b = new Instructor("Jan", "Nowak", 10);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
    @Test void courseUniqueness() {
        var s = new java.util.HashSet<Course>();
        s.add(new Course("Bachata", "Beginner", 20));
        s.add(new Course("Bachata", "Beginner", 30));
        assertEquals(1, s.size());
    }
}