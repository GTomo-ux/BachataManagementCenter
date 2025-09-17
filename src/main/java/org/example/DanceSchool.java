package org.example;


import org.example.exceptions.CourseFullException;
import org.example.exceptions.NotEnrolledException;
import org.example.exceptions.ScheduleConflictException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class DanceSchool implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final DanceSchool instance = new DanceSchool();
    private Set<Student> students = new HashSet<>();
    private Map<Integer, Student> studentMap = new HashMap<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();
    private int nextStudentId = 0;


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
        if (student.getID() < 0) {
            student.setID(nextStudentId++);
        } else {
            if (student.getID() >= nextStudentId) {
                nextStudentId = student.getID() + 1;
            }
        }
        if (this.students.add(student)) {
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
    public void addLesson (Lesson lesson) throws ScheduleConflictException {
        if (!(this.lessons.contains(lesson))) {
            for (Lesson lesson1: this.lessons) {
                if (lesson1.getRoom() == lesson.getRoom()) {
                    boolean overlap = !(lesson1.getEndTime().isBefore(lesson.getStartTime()) || lesson1.getStartTime().isAfter(lesson.getEndTime()));
                    if (overlap) {
                        throw new ScheduleConflictException("Conflict! Room no. " + lesson1.getRoom().getID(lesson1.getRoom()) + " is already occupied.");
                    }
                }
            }
            this.lessons.add(lesson);
        }
    }

    // ZAPIS
    public void saveData () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 1. Studenci
        try (PrintWriter writer = new PrintWriter(new FileWriter("Students"))) {
            for (Student student: this.students) {
                writer.println(student.getName() + "," + student.getSurname() + "," + student.getID());
            }
        } catch (IOException e) {
            System.out.println("Students enrollment error: " + e.getMessage());
        }

        // 2. Instruktorzy.
        try (PrintWriter writer = new PrintWriter(new FileWriter("Instructors"))) {
            for (Instructor instructor: this.instructors) {
                writer.println(instructor.getName() + "," + instructor.getSurname() + "," + instructor.getExperience());
            }
        } catch (IOException e) {
            System.out.println("Instructors enrollment error: " + e.getMessage());
        }

        // 3. Kursy.
        try (PrintWriter writer = new PrintWriter(new FileWriter("Courses"))) {
            for (Course course: this.courses) {
                writer.println(course.getName() + "," + course.getLevel() + "," + course.getLimitOfPlaces());
            }
        } catch (IOException e) {
            System.out.println("Courses enrollment error: " + e.getMessage());
        }

        // 4. Lekcje.
        try (PrintWriter writer = new PrintWriter(new FileWriter("Lessons"))) {
            for (Lesson lesson: this.lessons) {
                writer.println(lesson.getStartTime().format(formatter) + "," +
                        lesson.getDuration().toMinutes() + "," +
                        lesson.getRoom());
            }
        } catch (IOException e) {
            System.out.println("Lessons enrollment error: " + e.getMessage());
        }

        // Serializacja szkoły.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("DanceSchool.ser"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Serialization error: " + e.getMessage());
        }

    }

    // WCZYTYWANIE
    public static DanceSchool loadData () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 1. Próba serializacji.
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("DanceSchool.ser"))) {
            return (DanceSchool) ois.readObject();
        } catch (Exception e) {
            System.out.println("Failed to load from serialization, trying CSV...");
        }

        // 2. Wczytanie z CSV.
        DanceSchool ds = getInstance();

        // 2.1 Studenci.
        try (Scanner scanner = new Scanner(new File("Students"))) {
            int maxId = -1;
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                Student s = new Student(parts[0], parts[1]);
                int id = Integer.valueOf(parts[2]);
                s.setID(id);
                ds.getStudents().add(s);
                ds.studentMap.put(id,s);
                if (id > maxId) {
                    maxId = id;
                }
            }
            ds.nextStudentId = maxId + 1;
        } catch (Exception e) {
            System.out.println("Student loading error: " + e.getMessage());
        }

        // 2.2 Instruktorzy.
        try (Scanner sc = new Scanner(new File("Instructors"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                ds.addInstructor(new Instructor(parts[0], parts[1], Integer.valueOf(parts[2])));
            }
        } catch (Exception e) {
            System.out.println("Error loading instructors: " + e.getMessage());
        }

        // 2.3 Kursy.
        try (Scanner sc = new Scanner(new File("Courses"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                ds.addCourse(new Course(parts[0], parts[1], Integer.valueOf(parts[2])));
            }
        } catch (Exception e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }

        // 2.4 Lekcje.
        try (Scanner sc = new Scanner(new File("Lessons"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                LocalDateTime start = LocalDateTime.parse(parts[0], formatter);
                Duration duration = Duration.ofMinutes(Long.parseLong(parts[1]));
                Room room = Room.valueOf(parts[2].trim());
                ds.addLesson(new Lesson(start, duration, room));
            }
        } catch (Exception e) {
            System.out.println("Error loading lessons: " + e.getMessage());
        }

        return ds;
    }

    public void addLessonToTheCourse (Lesson lesson, String name) {
        int index = -1;
        for (int i = 0; i < this.courses.size(); i++) {
            if (this.courses.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("\n" + "No course named: " + name);
        }

        if (!(this.courses.get(index).getLessons().contains(lesson))) {
            if ((lesson.getCourse() == null)) {
                lesson.setCourse(this.courses.get(index));
                this.courses.get(index).addLesson(lesson);
            }

        }
    }


    public void studentToTheCourse (Student student, String name) throws CourseFullException {
        int index = -1;
        for (int i = 0; i < this.courses.size(); i++) {
            if (this.courses.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("\n" + "No course named: " + name);
        }
        if (this.courses.get(index).getStudents().contains(student)) {
            return;
        }

        if (this.courses.get(index).getStudents().size() >= this.courses.get(index).getLimitOfPlaces()) {
            throw new CourseFullException("The course is now full!");
        }
        if (index != -1) {
            student.add(this.courses.get(index));
            this.courses.get(index).addStudent(student);
        }




    }
    public void removeStudentFromCourse (Student student, String name) throws NotEnrolledException {
        int index = -1;
        for (int i = 0; i <this.courses.size(); i++) {
            if (this.courses.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("No course named: " + name);
        }
        int studentNumber = -1;
        for (int i = 0; i < this.courses.get(index).getStudents().size(); i++) {
            if (this.courses.get(index).getStudents().get(i).getName().equals(student.getName()) &&this.courses.get(index).getStudents().get(i).getSurname().equals(student.getSurname())) {
                studentNumber = i;
                break;
            }
        }
        if (studentNumber == -1) {
            throw new NotEnrolledException("The student was not enrolled in this course, so the student cannot be removed!");
        }
        student.remove(this.courses.get(index));
        this.courses.get(index).getStudents().remove(studentNumber);


    }
    public void instructorToTheCourse (Instructor instructor, String name) {
        int index = -1;
        for (int i = 0; i < this.courses.size(); i++) {
            if (this.courses.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("No course named: " + name);
        }
        if (index != -1) {
            if (this.courses.get(index).getInstructor() == null) {
                instructor.add(this.courses.get(index));
                this.courses.get(index).setInstructor(instructor);
            }
        }

    }


    // API Statistics methods



}
