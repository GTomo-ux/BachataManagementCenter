package org.example;

import org.example.exceptions.CourseFullException;
import org.example.exceptions.LessonEnrollWithNoCourseException;
import org.example.exceptions.NotEnrolledException;
import org.example.exceptions.ScheduleConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

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
        var c1 = new Course("Bachata", "Beginner", 1, null);
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
        var course = new Course("Salsa", "Beginner", 20, null);
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
    @Test
    public void addStudentToTheLesson_LessonEnrollWithNoCourseException() {
        Course course = new Course("Bachata Influence", "Beginner", 100);
        Course course2 = new Course("Bachata Dominicana", "Beginner", 100);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Lesson lesson = new Lesson(LocalDateTime.of(2025,8,02,11,23),Duration.ofMinutes(90), Room.ROOM1);
        String date = "2025-09-12 12:21";
        Lesson lesson2 = new Lesson(LocalDateTime.parse("2025-09-10 12:21", formatter), Duration.ofMinutes(90), Room.ROOM1);
        Lesson lesson3 = new Lesson(LocalDateTime.parse(date, formatter), Duration.ofMinutes(90), Room.ROOM1);
        Student student = new Student("Adam", "Gacek");
        Student student2 = new Student("Gienek", "Wolny");
        service.addCourse(course);
        service.addCourse(course2);
        service.addStudent(student.getName(), student.getSurname());
        service.addStudent(student2.getName(), student2.getSurname());
        try {
            service.addLesson(lesson);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            service.addLesson(lesson2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            service.addLesson(lesson3);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        service.addLessonToTheCourse(lesson, "Bachata Influence", "Beginner");
        service.addLessonToTheCourse(lesson2, "Bachata Influence", "Beginner");
        service.addLessonToTheCourse(lesson3, "Bachata Influence", "Beginner");


        try {
            service.studentToTheCourse(student, "Bachata Influence", "Beginner");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


        assertThrows(LessonEnrollWithNoCourseException.class, () -> service.addStudentToTheLesson(student2, lesson.getStartTime(), lesson.getRoom()),
                "Powinno rzucic wyjątek ze student musi się najpierw zapisać na kurs");
        assertDoesNotThrow(() -> service.addStudentToTheLesson(student, lesson.getStartTime(), lesson.getRoom()));

    }
}