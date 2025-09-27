package org.example;

import java.io.Console;
import java.time.LocalDate;
import java.util.Scanner;

public class UserInterface {
    private final DanceSchoolService service;
    private final Scanner sc;
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "secret123";

    public UserInterface(DanceSchoolService service) {
        this.service = service;
        this.sc = new Scanner(System.in);
    }
    public void run() {
        Panel current = Panel.MAIN;
        while (current != Panel.EXIT) {
            switch (current) {
                case MAIN       -> current = mainPanel();
                case STUDENT    -> current = studentPanel();
                case SCHEDULE   -> current = schedulePanel();
                case PAYMENTS   -> current = paymentsPanel();
                case INSTRUCTORS-> current = instructorsPanel();
                case COURSES    -> current = coursesPanel();
                case STATS      -> current = statsPanel();
                case ADMIN      -> current = adminPanel();
            }
        }
        System.out.println("Bye!");
    }

    private Panel mainPanel() {
        while (true) {
            clear();
            System.out.println("""
                === MAIN ===
                1) Student Panel
                2) Schedules
                3) Instructors
                4) Courses
                5) Statistics
                6) Payment Methods
                7) Admin Panel
                0) Exit
                """);
            String c = readLine("> ");
            switch (c) {
                case "1" -> { return Panel.STUDENT; }
                case "2" -> { return Panel.SCHEDULE; }
                case "3" -> { return Panel.INSTRUCTORS; }
                case "4" -> { return Panel.COURSES; }
                case "5" -> { return Panel.STATS; }
                case "6" -> { return Panel.PAYMENTS; }
                case "7" -> {
                    if (requireAdminAuth()) return Panel.ADMIN;
                    System.out.println("Access denied. Press Enter to return to main menu...");
                    sc.nextLine();
                    return Panel.MAIN;
                }
                case "0" -> { return Panel.EXIT; }
                default  -> {
                    System.out.println("Wrong choice. Enter...");
                    sc.nextLine();
                }
            }
        }
    }
    private Panel studentPanel() {
        while (true) {
            clear();
            System.out.println("""
                === STUDENT ===
                1) Register student
                2) Enroll student to course
                3) Enroll student to lesson
                4) Unenroll student from course
                5) Unenroll student from lesson
                6) Find my profile (ID / Name+Surname)
                0) Back
                """);
            String c = readLine("> ");
            try {
                switch (c) {
                    case "1" -> {
                        String name = readLine("Name: ");
                        String surname = readLine("Surname: ");
                        var s = service.addStudent(name, surname);
                        service.saveData();
                        System.out.println("Registered: " + s);
                        System.out.println("Enter...");
                        sc.nextLine();
                    }
                    case "2" -> {
                        String name = readLine("Student name: ");
                        String surname = readLine("Student surname: ");
                        String cname = readLine("Course name: ");
                        String level = readLine("Course level: ");
                        var student = service.findStudentByNameAndSurname(name, surname);
                        service.studentToTheCourse(student, cname, level);
                        service.saveData();
                        System.out.println("Enrolled to course.");
                        System.out.println("Enter...");
                        sc.nextLine();
                    }
                    case "3" -> {
                        String name = readLine("Student name: ");
                        String surname = readLine("Student surname: ");
                        System.out.println("Lesson date:");
                        var date = readDate();
                        int hh = readIntInRange("Hour (0-23): ", 0, 23);
                        int mm = readIntInRange("Minute (0-59): ", 0, 59);
                        var room = pickRoom();
                        var student = service.findStudentByNameAndSurname(name, surname);
                        var dt = date.atTime(hh, mm);
                        service.addStudentToTheLesson(student, dt, room);
                        service.saveData();
                        System.out.println("Enrolled to lesson.");
                        System.out.println("Enter...");
                        sc.nextLine();
                    }
                    case "4" -> {
                        String name = readLine("Student name: ");
                        String surname = readLine("Student surname: ");
                        String cname = readLine("Course name: ");
                        String level = readLine("Course level: ");
                        var student = service.findStudentByNameAndSurname(name, surname);
                        service.removeStudentFromCourse(student, cname, level);
                        service.saveData();
                        System.out.println("Unenrolled from course.");
                        System.out.println("Enter...");
                        sc.nextLine();
                    }
                    case "5" -> {
                        String name = readLine("Student name: ");
                        String surname = readLine("Student surname: ");
                        System.out.println("Lesson date:");
                        var date = readDate();
                        int hh = readIntInRange("Hour (0-23): ", 0, 23);
                        int mm = readIntInRange("Minute (0-59): ", 0, 59);
                        var room = pickRoom();
                        var student = service.findStudentByNameAndSurname(name, surname);
                        var dt = date.atTime(hh, mm);
                        service.removeStudentFromLesson(student, dt, room);
                        service.saveData();
                        System.out.println("Unenrolled from lesson.");
                        System.out.println("Enter...");
                        sc.nextLine();
                    }
                    case "6" -> {
                        Student s = findStudentFlow();
                        if (s != null) printStudentWithCourses(s);
                    }
                    case "0" -> { return Panel.MAIN; }
                    default -> System.out.println("Wrong choice. Enter...");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Enter...");
                sc.nextLine();
            }
        }
    }

    private Room pickRoom() {
        while (true) {
            String r = readLine("Room (1/2): ");
            if (r.equals("1")) return Room.ROOM1;
            if (r.equals("2")) return Room.ROOM2;
            System.out.println("Unknown room.");
        }
    }
    private Panel schedulePanel() {
        while (true) {
            clear();
            System.out.println("""
                === SCHEDULES ===
                1) Daily
                2) Weekly
                3) By room
                4) By instructor
                5) By student
                0) Back
                """);
            String c = readLine("> ");
            switch (c) {
                case "1" -> dailySchedule();
                case "2" -> weeklySchedule();
                case "3" -> roomSchedule();
                case "4" -> instructorSchedule();
                case "5" -> studentSchedule();
                case "0" -> { return Panel.MAIN; }
                default -> { System.out.println("Wrong choice. Enter..."); sc.nextLine(); }
            }
        }
    }

    private void dailySchedule() {
        clear();
        System.out.println("Choose date:");
        var date = readDate();
        clear();
        System.out.println(service.printDailySchedule(date));
        System.out.println("0) Back");
        readLine("> ");
    }

    private void weeklySchedule() {
        clear();
        System.out.println("Week start date:");
        var start = readDate();
        clear();
        System.out.println(service.printWeeklySchedule(start));
        System.out.println("0) Back");
        readLine("> ");
    }

    private void roomSchedule() {
        clear();
        System.out.println("Date for room schedule:");
        var date = readDate();
        var room = pickRoom();
        clear();
        System.out.println(service.printRoomSchedule(date, room));
        System.out.println("0) Back");
        readLine("> ");
    }

    private void instructorSchedule() {
        clear();
        System.out.println("Date:");
        var date = readDate();
        String name = readLine("Instructor name: ");
        String surname = readLine("Instructor surname: ");
        clear();
        System.out.println(service.printInstructorSchedule(date, name, surname));
        System.out.println("0) Back");
        readLine("> ");
    }

    private void studentSchedule() {
        clear();
        System.out.println("Date:");
        var date = readDate();
        String name = readLine("Student name: ");
        String surname = readLine("Student surname: ");
        clear();
        System.out.println(service.printStudentSchedule(date, name, surname));
        System.out.println("0) Back");
        readLine("> ");
    }

    private Panel paymentsPanel() {
        while (true) {
            clear();
            System.out.println("""
                === PAYMENT METHODS ===

                A) Single Entry — 30 PLN/entry
                B) Pass — 25 PLN/entry (min. 4, without max. limit)
                C) Monthly Unlimited — 200 PLN/month

                (Payment in advance.)
                0) Back
                """);
            String c = readLine("> ");
            if ("0".equals(c)) return Panel.MAIN;
        }
    }
    private Panel instructorsPanel() {
        while (true) {
            clear();
            System.out.println("=== INSTRUCTORS ===\n");
            service.getSchool().getInstructors().forEach(System.out::println);
            System.out.println("\n0) Back");
            if ("0".equals(readLine("> "))) return Panel.MAIN;
        }
    }

    private Panel coursesPanel() {
        while (true) {
            clear();
            System.out.println("=== COURSES ===\n");
            service.getSchool().getCourses().forEach(System.out::println);
            System.out.println("\n0) Back");
            if ("0".equals(readLine("> "))) return Panel.MAIN;
        }
    }
    private Panel statsPanel() {
        while (true) {
            clear();
            System.out.println("""
                === STATS ===
                1) Most popular instructor
                2) Most active student
                3) Top courses
                0) Back
                """);
            String c = readLine("> ");
            switch (c) {
                case "1" -> { clear(); System.out.println(service.stat(StatType.MOST_POPULAR_INSTRUCTOR)); pause(); }
                case "2" -> { clear(); System.out.println(service.stat(StatType.MOST_ACTIVE_STUDENT)); pause(); }
                case "3" -> { clear(); System.out.println(service.stat(StatType.TOP_COURSES)); pause(); }
                case "0" -> { return Panel.MAIN; }
                default -> { System.out.println("Wrong choice. Enter..."); sc.nextLine(); }
            }
        }
    }

    private void pause() {
        System.out.println("\n0) Back");
        readLine("> ");
    }
    private Panel adminPanel() {
        while (true) {
            clear();
            System.out.println("""
                    === ADMIN ===
                    1) Add student
                    2) Add instructor
                    3) Add course
                    4) Add lesson
                    5) Assign instructor to course
                    6) Enroll student to course
                    7) Assign lesson to course
                    8) Remove instructor from course
                    9) Remove student from course
                    10) Remove lesson from course
                    11) Remove student/instructor/course/lesson
                    12) Display student/instructor/course
                    0) Back
                    """);
            String c = readLine("> ");
            try {
                switch (c) {
                    case "1" -> { // Add student
                        var s = service.addStudent(readLine("Name: "), readLine("Surname: "));
                        service.saveData();
                        System.out.println("Added: " + s); sc.nextLine();
                    }
                    case "2" -> {
                        var i = new Instructor(readLine("Name: "), readLine("Surname: "),
                                readInt("Experience (years): "));
                        service.addInstructor(i);
                        service.saveData();
                        System.out.println("Added instructor."); sc.nextLine();
                    }
                    case "3" -> {
                        String name = readLine("Course name: ");
                        String level = readLine("Level (e.g. Beginner/Intermediate): ");
                        int limit = readInt("Limit: ");
                        service.addCourse(new Course(name, level, limit));
                        service.saveData();
                        System.out.println("Added course."); sc.nextLine();
                    }
                    case "4" -> {
                        System.out.println("Lesson start date:");
                        var date = readDate();
                        int hh = readIntInRange("Hour (0-23): ", 0, 23);
                        int mm = readIntInRange("Minute (0-59): ", 0, 59);
                        int dur = readIntInRange("Duration (minutes): ", 1, 600);
                        var room = pickRoom();
                        var lesson = new Lesson(date.atTime(hh, mm),
                                java.time.Duration.ofMinutes(dur),
                                room);
                        service.addLesson(lesson);
                        service.saveData();
                        System.out.println("Added lesson."); sc.nextLine();
                    }
                    case "5" -> {
                        String in = readLine("Instructor name: ");
                        String is = readLine("Instructor surname: ");
                        String cn = readLine("Course name: ");
                        String lv = readLine("Course level: ");
                        service.instructorToTheCourse(service.findInstructorByNameAndSurname(in, is), cn, lv);
                        service.saveData();
                        System.out.println("Assigned."); sc.nextLine();
                    }
                    case "6" -> {
                        String sn = readLine("Student name: ");
                        String ss = readLine("Student surname: ");
                        String cn = readLine("Course name: ");
                        String lv = readLine("Course level: ");
                        service.studentToTheCourse(service.findStudentByNameAndSurname(sn, ss), cn, lv);
                        service.saveData();
                        System.out.println("Enrolled."); sc.nextLine();
                    }
                    case "7" -> {
                        System.out.println("Pick existing lesson to assign:");
                        var date = readDate();
                        int hh = readIntInRange("Hour (0-23): ", 0, 23);
                        int mm = readIntInRange("Minute (0-59): ", 0, 59);
                        var room = pickRoom();
                        String cn = readLine("Course name: ");
                        String lv = readLine("Course level: ");
                        var lesson = service.findLesson(date.atTime(hh, mm), room);
                        service.addLessonToTheCourse(lesson, cn, lv);
                        service.saveData();
                        System.out.println("Lesson assigned to course."); sc.nextLine();
                    }
                    case "8" -> {
                        String in = readLine("Instructor name: ");
                        String is = readLine("Instructor surname: ");
                        String cn = readLine("Course name: ");
                        String lv = readLine("Course level: ");
                        service.removeInstructorFromTheCourse(service.findInstructorByNameAndSurname(in, is), cn, lv);
                        service.saveData();
                        System.out.println("Instructor removed from course."); sc.nextLine();
                    }
                    case "9" -> {
                        String sn = readLine("Student name: ");
                        String ss = readLine("Student surname: ");
                        String cn = readLine("Course name: ");
                        String lv = readLine("Course level: ");
                        service.removeStudentFromCourse(service.findStudentByNameAndSurname(sn, ss), cn, lv);
                        service.saveData();
                        System.out.println("Student removed from course."); sc.nextLine();
                    }
                    case "10" -> {
                        System.out.println("Pick lesson to detach from course:");
                        var date = readDate();
                        int hh = readIntInRange("Hour (0-23): ", 0, 23);
                        int mm = readIntInRange("Minute (0-59): ", 0, 59);
                        var room = pickRoom();
                        String cn = readLine("Course name: ");
                        String lv = readLine("Course level: ");
                        var lesson = service.findLesson(date.atTime(hh, mm), room);
                        service.removeLessonFromTheCourse(lesson, cn, lv);
                        service.saveData();
                        System.out.println("Lesson removed from course."); sc.nextLine();
                    }
                    case "11" -> {
                        adminRemovePanel();
                    }
                    case "12" -> {
                        adminViewPanel();
                    }
                    case "0" -> { return Panel.MAIN; }
                    default -> { System.out.println("Wrong choice. Enter..."); sc.nextLine(); }
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Enter..."); sc.nextLine();
            }
        }
    }
    private void adminViewPanel() {
        while (true) {
            clear();
            System.out.println("""
                === VIEW ===
                1) Instructors
                2) Courses
                3) Schedules (same as main menu)
                4) Students
                0) Back
                """);
            String c = readLine("> ");
            switch (c) {
                case "1" -> {
                    clear();
                    System.out.println("=== INSTRUCTORS ===\n");
                    service.getSchool().getInstructors().forEach(System.out::println);
                    pause();
                }
                case "2" -> {
                    clear();
                    System.out.println("=== COURSES ===\n");
                    service.getSchool().getCourses().forEach(System.out::println);
                    pause();
                }
                case "3" -> {
                    schedulePanel();
                }
                case "4" -> {
                    adminViewStudentsPanel();
                }
                case "0" -> { return; }
                default -> { System.out.println("Wrong choice. Press Enter..."); sc.nextLine(); }
            }
        }
    }

    private void adminViewStudentsPanel() {
        while (true) {
            clear();
            System.out.println("""
                === STUDENTS VIEW ===
                1) List all students (with courses)
                2) Find single student (ID / Name+Surname)
                0) Back
                """);
            String c = readLine("> ");
            switch (c) {
                case "1" -> {
                    clear();
                    if (service.getSchool().getStudents().isEmpty()) {
                        System.out.println("No students.");
                    } else {
                        for (Student s : service.getSchool().getStudents()) {
                            System.out.println("-".repeat(50));
                            System.out.println(s.getName() + " " + s.getSurname() + " (ID: " + s.getID() + ")");
                            if (s.getCourses().isEmpty()) {
                                System.out.println("  • none");
                            } else {
                                for (Course c2 : s.getCourses()) {
                                    String instr = (c2.getInstructor() == null)
                                            ? "instructor: (not assigned)"
                                            : "instructor: " + c2.getInstructor().getName() + " " + c2.getInstructor().getSurname();
                                    System.out.println("  • " + c2.getName() + " " + c2.getLevel() + " — " + instr);
                                }
                            }
                        }
                        System.out.println("-".repeat(50));
                    }
                    pause();
                }
                case "2" -> {
                    Student s = findStudentFlow();
                    if (s != null) printStudentWithCourses(s);
                }
                case "0" -> { return; }
                default -> { System.out.println("Wrong choice. Press Enter..."); sc.nextLine(); }
            }
        }
    }

    private void adminRemovePanel() {
        clear();
        System.out.println("""
            Remove:
            1) Student
            2) Instructor
            3) Course
            4) Lesson
            0) Back
            """);
        String c = readLine("> ");
        try {
            switch (c) {
                case "1" -> {
                    service.removeStudent(readLine("Name: "), readLine("Surname: "));
                    service.saveData();
                    System.out.println("Removed student."); sc.nextLine();
                }
                case "2" -> {
                    service.removeInstructor(readLine("Name: "), readLine("Surname: "));
                    service.saveData();
                    System.out.println("Removed instructor."); sc.nextLine();
                }
                case "3" -> {
                    service.removeCourse(readLine("Course name: "), readLine("Level: "));
                    service.saveData();
                    System.out.println("Removed course."); sc.nextLine();
                }
                case "4" -> {
                    System.out.println("Lesson date:");
                    var date = readDate();
                    int hh = readIntInRange("Hour (0-23): ", 0, 23);
                    int mm = readIntInRange("Minute (0-59): ", 0, 59);
                    var room = pickRoom();
                    service.removeLesson(date.atTime(hh, mm), room);
                    service.saveData();
                    System.out.println("Removed lesson."); sc.nextLine();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage()); sc.nextLine();
        }
    }


    // POMOCNICZE
    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Is is not the number. Try again."); }
        }
    }

    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v < min || v > max) System.out.println("The value is out of scope ("+min+"-"+max+").");
            else return v;
        }
    }

    private java.time.LocalDate readDate() {
        while (true) {
            int y = readIntInRange("Year: ", 1900, 2100);
            int m = readIntInRange("Month: ", 1, 12);
            int d = readIntInRange("Day: ", 1, 31);
            try { return java.time.LocalDate.of(y, m, d); }
            catch (java.time.DateTimeException e) { System.out.println("Wrong date. Try again."); }
        }
    }

    private String readPassword(String prompt) {
        Console cons = System.console();
        if (cons != null) {
            char[] pwd = cons.readPassword(prompt);
            return pwd == null ? "" : new String(pwd);
        }
        return readLine(prompt);
    }

    private boolean requireAdminAuth() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            clear();
            System.out.println("=== ADMIN LOGIN === (attempt " + attempt + " of 3)");
            String u = readLine("Login: ");
            String p = readPassword("Password: ");
            if (ADMIN_USER.equals(u) && ADMIN_PASS.equals(p)) {
                return true;
            }
            System.out.println("Invalid credentials. Press Enter to retry...");
            sc.nextLine();
        }
        return false;
    }
    private void printStudentWithCourses(Student s) {
        clear();
        System.out.println("Student: " + s.getName() + " " + s.getSurname() + " (ID: " + s.getID() + ")");
        System.out.println("Enrolled courses:");
        if (s.getCourses().isEmpty()) {
            System.out.println("  — none");
        } else {
            for (Course c : s.getCourses()) {
                String instr = (c.getInstructor() == null)
                        ? "instructor: (not assigned)"
                        : "instructor: " + c.getInstructor().getName() + " " + c.getInstructor().getSurname();
                System.out.println("  • " + c.getName() + " " + c.getLevel() + " — " + instr);
            }
        }
        System.out.println("\n0) Back");
        readLine("> ");
    }

    private Student findStudentFlow() {
        while (true) {
            clear();
            System.out.println("""
                Find a student:
                1) By ID
                2) By name and surname
                0) Back
                """);
            String c = readLine("> ");
            try {
                switch (c) {
                    case "1" -> {
                        int id = readInt("ID: ");
                        Student s = service.findStudentByID(id);
                        if (s == null) {
                            System.out.println("No student with ID " + id + ". Press Enter...");
                            sc.nextLine();
                            continue;
                        }
                        return s;
                    }
                    case "2" -> {
                        String name = readLine("Name: ");
                        String surname = readLine("Surname: ");
                        return service.findStudentByNameAndSurname(name, surname);
                    }
                    case "0" -> { return null; }
                    default -> System.out.println("Wrong choice. Press Enter...");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Press Enter...");
                sc.nextLine();
            }
        }
    }
}
