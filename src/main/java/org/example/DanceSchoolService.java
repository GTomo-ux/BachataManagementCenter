package org.example;

import org.example.exceptions.CourseFullException;
import org.example.exceptions.NotEnrolledException;
import org.example.exceptions.ScheduleConflictException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DanceSchoolService {

    private final DanceSchool school = DanceSchool.getInstance();
    private int nextStudentId = 0;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final CourseFactory courseFactory = new ExtendedCourseFactory();

    public DanceSchoolService() {
        // Opcjonalnie: auto wczytanie ist danych
        // loadData();
        recomputeNextStudentId();
    }

    private void recomputeNextStudentId() {
        this.nextStudentId = school.getStudents().stream()
                .mapToInt(Student::getID)
                .max().orElse(-1) + 1;
    }

    // Operacje na modelu, walidacje itd.

    public Student addStudent(String name, String surname) {
        Student s = new Student(name, surname);
        s.setID(nextStudentId++);
        school.addStudent(s);
        return s;
    }

    public void addInstructor(Instructor instructor) {
        school.addInstructor(instructor);
    }

    public void addCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        school.addCourse(course);
    }

    public void addLesson(Lesson lesson) throws ScheduleConflictException {
        for (Lesson existing : school.getLessons()) {
            if (existing.getRoom() == lesson.getRoom()) {
                boolean overlap = !(existing.getEndTime().isBefore(lesson.getStartTime())
                        || existing.getStartTime().isAfter(lesson.getEndTime()));
                if (overlap) {
                    throw new ScheduleConflictException("Conflict! Room no. "
                            + existing.getRoom() + " is already occupied.");
                }
            }
        }
        school.addLessonInternal(lesson);
    }

    public void addLessonToTheCourse(Lesson lesson, String name) {
        Course c = findCourseByName(name);
        if (!c.getLessons().contains(lesson) && lesson.getCourse() == null) {
            lesson.setCourse(c);
            c.addLesson(lesson);
        }
    }

    public void studentToTheCourse(Student student, String name) throws CourseFullException {
        Course c = findCourseByName(name);
        if (c.getStudents().contains(student)) return;

        if (c.getStudents().size() >= c.getLimitOfPlaces()) {
            throw new CourseFullException("The course is now full!");
        }
        student.add(c);
        c.addStudent(student);
    }

    public void removeStudentFromCourse(Student student, String name) throws NotEnrolledException {
        Course c = findCourseByName(name);
        if (!c.getStudents().remove(student)) {
            throw new NotEnrolledException("The student was not enrolled in this course, so cannot be removed!");
        }
        student.remove(c);
    }

    public void instructorToTheCourse(Instructor instructor, String name) {
        Course c = findCourseByName(name);
        if (c.getInstructor() == null) {
            instructor.add(c);
            c.setInstructor(instructor);
        }
    }

    public Course createAndAddCourse(String name, CourseLevel level) {
        validateNameLevel(name, level);
        ensureCourseDoesNotExist(name, level);

        Course c = courseFactory.create(name, level);
        school.addCourse(c);
        return c;
    }
    public Course createAndAddCourse(String name, CourseLevel level, int limit) {
        validateNameLevel(name, level);
        if (limit <= 0) throw new IllegalArgumentException("Limit must be > 0");
        ensureCourseDoesNotExist(name, level);

        Course c = courseFactory.create(name, level, limit);
        school.addCourse(c);
        return c;
    }



    // ---------- Statystyki - Stream API ----------

    public String stat(StatType type) {
        switch (type) {
            case MOST_POPULAR_INSTRUCTOR: return mostPopularInstructor();
            case MOST_ACTIVE_STUDENT:     return mostActiveStudent();
            case TOP_COURSES:             return topCourses();
            default: return "Unknown query.";
        }
    }

    public String mostPopularInstructor () {
        if (school.getCourses().isEmpty()) {
            return "No data about courses.";
        }
        Map<Instructor,Integer> byInstructor = school.getCourses().stream()
                .filter(course -> course.getInstructor() != null)
                .collect(
                        Collectors.toMap(
                                Course::getInstructor,
                                c->c.getStudents() == null ? 0 : c.getStudents().size(),
                                Integer::sum,
                                HashMap::new
                        )
                );

        List<Map.Entry<Instructor,Integer>> sorted = byInstructor.entrySet().stream()
                .sorted(Map.Entry.<Instructor,Integer>comparingByValue(java.util.Comparator.reverseOrder()))
                .toList();

        if (sorted.isEmpty()) return "No course has an assigned instructor.";

        Map.Entry<Instructor,Integer> top = sorted.get(0);
        Instructor i = top.getKey();
        return i.getName() + " " + i.getSurname() + " — " + top.getValue() + " registrations";




    }
    public String mostActiveStudent () {

        Map <Student, Integer> byStudent = school.getStudents().stream()
                .collect(Collectors.toMap(Function.identity(), student -> student.getCourses() == null ? 0 : student.getCourses().size()));

        List<Map.Entry<Student, Integer>> sorted = byStudent.entrySet().stream()
                .sorted(Map.Entry.<Student,Integer> comparingByValue().reversed())
                .collect(Collectors.toList());
        Student best = sorted.get(0).getKey();
        int howManyCourses = sorted.get(0).getValue();

        return best.getName() + " " + best.getSurname() + " - enrolled in " + howManyCourses + " courses";

    }
    public String topCourses () {
        Map <Course, Integer> byCourses = school.getCourses().stream()
                .collect(Collectors.toMap(Function.identity(), course -> course.getStudents() == null ? 0 : course.getStudents().size()));
        List<Map.Entry<Course, Integer>> sorted = byCourses.entrySet().stream()
                .sorted(Map.Entry.<Course, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        return "Top 3 most popular courses: \n" +
                "1. " + sorted.get(0).getKey().getName() + ", " + sorted.get(0).getKey().getLevel() + " - " + sorted.get(0).getValue() + " enrolled students\n" +
                "2. " + sorted.get(1).getKey().getName() + ", " + sorted.get(1).getKey().getLevel() + " - " + sorted.get(1).getValue() + " enrolled students\n" +
                "3. " + sorted.get(2).getKey().getName() + ", " + sorted.get(2).getKey().getLevel() + " - " + sorted.get(2).getValue() + " enrolled students";
    }

    // ---------- I/O: zapisz / wczytaj (prosto, jak miałeś) ----------

    public void saveData() {
        // 1. Students
        try (PrintWriter writer = new PrintWriter(new FileWriter("Students"))) {
            for (Student student: school.getStudents()) {
                writer.println(student.getName() + "," + student.getSurname() + "," + student.getID());
            }
        } catch (IOException e) {
            System.out.println("Students enrollment error: " + e.getMessage());
        }

        // 2. Instructors
        try (PrintWriter writer = new PrintWriter(new FileWriter("Instructors"))) {
            for (Instructor instructor: school.getInstructors()) {
                writer.println(instructor.getName() + "," + instructor.getSurname() + "," + instructor.getExperience());
            }
        } catch (IOException e) {
            System.out.println("Instructors enrollment error: " + e.getMessage());
        }

        // 3. Courses
        try (PrintWriter writer = new PrintWriter(new FileWriter("Courses"))) {
            for (Course course: school.getCourses()) {
                writer.println(course.getName() + "," + course.getLevel() + "," + course.getLimitOfPlaces());
            }
        } catch (IOException e) {
            System.out.println("Courses enrollment error: " + e.getMessage());
        }

        // 4. Lessons
        try (PrintWriter writer = new PrintWriter(new FileWriter("Lessons"))) {
            for (Lesson lesson: school.getLessons()) {
                writer.println(lesson.getStartTime().format(fmt) + "," +
                        lesson.getDuration().toMinutes() + "," +
                        lesson.getRoom());
            }
        } catch (IOException e) {
            System.out.println("Lessons enrollment error: " + e.getMessage());
        }

        // 5. Serializacja całej szkoły
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("DanceSchool.ser"))) {
            oos.writeObject(school);
        } catch (IOException e) {
            System.out.println("Serialization error: " + e.getMessage());
        }
    }

    public void loadData() {
        // 1) serializacja
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("DanceSchool.ser"))) {
            DanceSchool loaded = (DanceSchool) ois.readObject();
            // Przenieś dane do singletona (bez zmiany referencji)
            school.clearAll();
            for (Student s : loaded.getStudents()) school.addStudentInternal(s);
            for (Instructor i : loaded.getInstructors()) school.addInstructorInternal(i);
            for (Course c : loaded.getCourses()) school.addCourseInternal(c);
            for (Lesson l : loaded.getLessons()) school.addLessonInternal(l);
        } catch (Exception e) {
            // 2) CSV fallback
            school.clearAll();

            // Students
            try (Scanner scanner = new Scanner(new File("Students"))) {
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(",");
                    Student s = new Student(parts[0], parts[1]);
                    int id = Integer.parseInt(parts[2]);
                    s.setID(id);
                    school.addStudentInternal(s);
                }
            } catch (Exception ex) {
                System.out.println("Student loading error: " + ex.getMessage());
            }

            // Instructors
            try (Scanner sc = new Scanner(new File("Instructors"))) {
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    school.addInstructorInternal(new Instructor(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            } catch (Exception ex) {
                System.out.println("Error loading instructors: " + ex.getMessage());
            }

            // Courses
            try (Scanner sc = new Scanner(new File("Courses"))) {
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    school.addCourseInternal(new Course(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            } catch (Exception ex) {
                System.out.println("Error loading courses: " + ex.getMessage());
            }

            // Lessons
            try (Scanner sc = new Scanner(new File("Lessons"))) {
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    LocalDateTime start = LocalDateTime.parse(parts[0], fmt);
                    Duration duration = Duration.ofMinutes(Long.parseLong(parts[1]));
                    Room room = Room.valueOf(parts[2].trim());
                    school.addLessonInternal(new Lesson(start, duration, room));
                }
            } catch (Exception ex) {
                System.out.println("Error loading lessons: " + ex.getMessage());
            }
        }

        // po wczytaniu odtwarzamy nextStudentId
        recomputeNextStudentId();
    }

    // ---------- Pomocnicze ----------
    private Course findCourseByName(String name) {
        return school.getCourses().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("\nNo course named: " + name));
    }

    private void validateNameLevel(String name, CourseLevel level) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Course name cannot be empty");
        Objects.requireNonNull(level, "Course level cannot be null");
    }
    private void ensureCourseDoesNotExist(String name, CourseLevel level) {
        boolean exists = school.getCourses().stream()
                .anyMatch(c -> name.equals(c.getName())
                        && level.getLabel().equals(c.getLevel()));
        if (exists) {
            throw new IllegalStateException(
                    "Course already exists: " + name + " (" + level.getLabel() + ")"
            );
        }
    }

}
