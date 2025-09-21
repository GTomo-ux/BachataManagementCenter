package org.example;

import org.example.exceptions.CourseFullException;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;


public class Main {
    public static void main(String[] args) {

        DanceSchool ds = DanceSchool.loadData();


        ds.saveData();



        Map<String,Integer> scores = Map.of("Ala",90,"Ola",72,"Jan",95,"Piotr",60);

        List<String> names = scores.entrySet().stream()
                .filter(n -> n.getValue() >= 80)
                .map(n -> n.getKey())
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));

        Optional<String> topName = scores.entrySet().stream()
                .max(comparingByValue())
                .map(Map.Entry::getKey);

        List<Map.Entry<String,Integer>> reverseValue = scores.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
                .collect(Collectors.toCollection(ArrayList::new));

        /* Map<String, Integer> newScores = scores.entrySet().stream()
                        .map(c -> c.getValue() + 5)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, TreeMap::new));

        System.out.println(reverseValue);

        */









    }
}