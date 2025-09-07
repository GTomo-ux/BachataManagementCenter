package org.example;


import org.example.exceptions.CourseFullException;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.*;

public class DanceSchool {
    private static final DanceSchool instance = new DanceSchool();
    private Set<Student> students = new HashSet<>();
    private Map<Integer, Student> studentMap = new HashMap<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();
    private static int studentID = 0;


    private DanceSchool() {
    }


    public static DanceSchool getInstance() {
        return instance;
    }
    public Set<Student> getStudents () {
        return this.students;
    }
    public List<Instructor> getInstructors () {
        return this.instructors;
    }
    public List<Course> getCourses () {
        return this.courses;
    }
    public List<Lesson> getLessons () {
        return this.lessons;
    }


    public void addStudent(Student student) {
        if (!(students.contains(student))) {
            student.setID(studentID++);
            students.add(student);
            studentMap.put(student.getID(), student);
        }
    }
    public void addInstructor (Instructor instructor) {
        if (!(this.instructors.contains(instructor))) {
            this.instructors.add(instructor);
        }
    }
    public void addCourse (Course course) {
        if (!(courses.contains(course))) {
            courses.add(course);
        }
    }
    public void addLessonToTheCourse (Lesson lesson, String name) {
        int index = -1;
        for (int i = 0; i < this.courses.size(); i++) {
            if (this.courses.get(index).getName().equals(name)) {
                index = i;
            }
        }

        if (!(this.courses.get(index).getLessons().contains(lesson))) {
            if ((lesson.getCourse() == null)) {
                lesson.setCourse(this.courses.get(index));
                this.courses.get(index).addLesson(lesson);
            }

        }
    }
    public void addStudentFromFile (String file) {
        try (Scanner scanner = new Scanner(Paths.get(file))) {
            while (scanner.hasNextLine()) {
                String [] parts = scanner.nextLine().split(",");
                addStudent(new Student(parts[0], parts[1]));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void addInstructorFromFile (String file) {
        try (Scanner scanner = new Scanner(Paths.get(file))) {
            while (scanner.hasNextLine()) {
                String [] parts = scanner.nextLine().split(",");
                addInstructor(new Instructor(parts[0], parts[1], Integer.valueOf(parts[2])));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void addCourseFromFile (String file) {
        try (Scanner scanner = new Scanner(Paths.get(file))) {
            while (scanner.hasNextLine()) {
                String [] parts = scanner.nextLine().split(",");
                addCourse(new Course(parts[0], parts[1], Integer.valueOf(parts[2])));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void studentToTheCourse (Student student, String name) throws CourseFullException {
        int index = -1;
        for (int i = 0; i < this.courses.size(); i++) {
            if (this.courses.get(index).getName().equals(name)) {
                index = i;
            }
        }
        if (this.courses.get(index).getStudents().size() >= this.courses.get(index).getLimitOfPlaces()) {
            throw new CourseFullException("Kurs jest już pełny!");
        }
        if (index != -1) {
            student.add(this.courses.get(index));
            this.courses.get(index).addStudent(student);
        }




    }
    public void instructorToTheCourse (Instructor instructor, String name) {
        int index = -1;
        for (int i = 0; i < this.courses.size(); i++) {
            if (this.courses.get(index).getName().equals(name)) {
                index = i;
            }
        }
        if (index != -1) {
            if (this.courses.get(index).getInstructor() == null) {
                instructor.add(this.courses.get(index));
                this.courses.get(index).setInstructor(instructor);
            }
        }


    }


}
