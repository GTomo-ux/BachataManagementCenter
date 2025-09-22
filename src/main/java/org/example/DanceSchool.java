package org.example;


import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class DanceSchool implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final DanceSchool INSTANCE = new DanceSchool();

    private final Set<Student> students = new HashSet<>();
    private final Map<Integer, Student> studentMap = new HashMap<>();
    private final List<Instructor> instructors = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();

    private DanceSchool() {}

    public static DanceSchool getInstance() { return INSTANCE; }

    // Gettery (dla prostoty – zwracamy „gołe” kolekcje jak u Ciebie)
    public Set<Student> getStudents () { return this.students; }
    public List<Instructor> getInstructors () { return this.instructors; }
    public List<Course> getCourses () { return this.courses; }
    public List<Lesson> getLessons () { return this.lessons; }

    // Proste "public" add – BEZ walidacji (waliduje serwis)
    public void addStudent(Student student) {
        this.students.add(student);
        this.studentMap.put(student.getID(), student);
    }
    public void addInstructor(Instructor instructor) {
        if (!this.instructors.contains(instructor)) this.instructors.add(instructor);
    }
    public void addCourse(Course course) {
        if (!this.courses.contains(course)) this.courses.add(course);
    }

    // Lekcję dodajemy bez logiki kolizji - tę robi serwis
    void addLessonInternal(Lesson lesson) {
        if (!this.lessons.contains(lesson)) this.lessons.add(lesson);
    }

    // Pomocnicze do odczytu po ID i resetu stanu (używa serwis przy load)
    Student findStudentById(int id) {
        return studentMap.get(id);
    }

    void clearAll() {
        students.clear();
        studentMap.clear();
        instructors.clear();
        courses.clear();
        lessons.clear();
    }

    // Używane przy wczytywaniu z plików przez serwis:
    void addStudentInternal(Student s) {
        this.students.add(s);
        this.studentMap.put(s.getID(), s);
    }
    void addInstructorInternal(Instructor i) { addInstructor(i); }
    void addCourseInternal(Course c) { addCourse(c); }


}
