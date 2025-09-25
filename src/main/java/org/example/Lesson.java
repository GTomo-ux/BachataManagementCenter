package org.example;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class Lesson implements Serializable {
    private LocalDateTime localDateTime;
    private Duration duration;
    private Course course;
    private List<Student> students;
    private Room room;

    public Lesson (LocalDateTime localDateTime, Duration duration, Room room, Course course) {
        this.localDateTime = localDateTime;
        this.duration = duration;
        this.course = course;
        this.students = new ArrayList<>();
        this.room = room;

    }
    public Lesson (LocalDateTime localDateTime, Duration duration, Room room) {
        this.localDateTime = localDateTime;
        this.duration = duration;
        this.course = null;
        this.students = new ArrayList<>();
        this.room = room;

    }
    public LocalDateTime getStartTime () {
        return this.localDateTime;
    }
    public Duration getDuration () {
        return this.duration;
    }
    public LocalDateTime getEndTime () {
        return localDateTime.plus(duration);
    }
    public Room getRoom() {
        return room;
    }
    public void setCourse (Course course) {
        this.course = course;
    }
    public Course getCourse () {
        return this.course;
    }
    public List<Student> getStudents () {
        return this.students;
    }
    public void addStudent (Student student) {
        this.students.add(student);
    }
    public void removeStudent (Student student) {
        this.students.remove(student);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(localDateTime, lesson.localDateTime) && room == lesson.room;
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDateTime, room);
    }

    @Override
    public String toString () {
        if (this.course == null) {
            if (this.room == Room.ROOM1) {
                return "Lesson on " + this.getStartTime() + ", lasting " + this.getDuration() + " minutes, in the room no. 1.";
            } else {
                return "Lesson on " + this.getStartTime() + ", lasting " + this.getDuration() + " minutes, in the room no. 2.";
            }
        } else {
            if (this.room == Room.ROOM1) {
                return this.course.getName() + " lesson " + " on " + this.getStartTime() + ", lasting " + this.getDuration() + " minutes, in the room no. 1.";
            } else {
                return this.course.getName() + " lesson " + " on " + this.getStartTime() + ", lasting " + this.getDuration() + " minutes, in the room no. 2.";
            }
        }
    }





}
