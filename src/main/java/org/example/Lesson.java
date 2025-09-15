package org.example;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;

public class Lesson implements Serializable {
    private LocalDateTime localDateTime;
    private Duration duration;
    private Course course;
    private int numberOfStudents;
    private Room room;

    public Lesson (LocalDateTime localDateTime, Duration duration, Room room) {
        this.localDateTime = localDateTime;
        this.duration = duration;
        this.course = null;
        this.numberOfStudents = 0;
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
    public int getNumberOfStudents () {
        return this.numberOfStudents;
    }





}
