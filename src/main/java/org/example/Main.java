package org.example;

import org.example.exceptions.CourseFullException;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;


public class Main {
    public static void main(String[] args) {

        DanceSchoolService service = new DanceSchoolService();

        service.loadData();

        //Ekran główny
        //Panel Studenta
            //Zarejestruj sie
            //Zapisz się na kurs
            //Zapisz się na lekcje
            //Wypisz się z kursu
            //Wypisz się z lekcji
            //Wyświetl
        //Harmonogramy
            //Wyświetl harmonogramy
        //Instruktorzy
            //Wyświetl instruktorów
        //Kursy
            //Wyświetl kursy
        //Statystyki
            //Wyświetl statystyki
        //Metody płatności
            //Wyświetl metody płatności
        //Panel administratora
            //admin




        service.saveData();



    }
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void printMainScreen() {
    }

}