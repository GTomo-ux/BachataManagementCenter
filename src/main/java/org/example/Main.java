package org.example;

import org.example.exceptions.CourseFullException;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;


public class Main {
    public static void main(String[] args) {

        DanceSchoolService service = new DanceSchoolService();
        service.loadData();
        UserInterface userInterface = new UserInterface(service);
        userInterface.run();

    }


}