package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.exceptions.CourseFullException;
import org.junit.jupiter.api.Test;

public class DanceSchoolTest {

    @Test
    public void testAddStudent () {
        DanceSchool danceSchool = DanceSchool.getInstance();
        Student student = new Student("Marcin", "Pierzchała");
        danceSchool.addStudent(student);
        assertTrue(danceSchool.getStudents().contains(student), "Student powinien być dodany do szkoły");
    }

    @Test
    public void testStudentToTheCourse_ThrowsExceptionWhenFull() {
        DanceSchool danceSchool = DanceSchool.getInstance();
        Course c1 = new Course("Bachata", "Basic", 1);
        danceSchool.addCourse(c1);

        Student s1 = new Student("Gienek", "Loska");
        Student s2 = new Student("Zbigniew", "Fiodor");
        danceSchool.addStudent(s1);
        danceSchool.addStudent(s2);

        assertDoesNotThrow(() -> danceSchool.studentToTheCourse(s1, "Bachata"));

        assertThrows(CourseFullException.class, () -> danceSchool.studentToTheCourse(s2, "Bachata"), "Powinno rzucić wyjątek przy próbie zapisania nad limit");

    }
}
