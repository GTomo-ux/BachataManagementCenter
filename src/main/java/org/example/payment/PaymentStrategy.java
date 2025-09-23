package org.example.payment;

public interface PaymentStrategy {
    String getName(); //Nazwa typu płatności, np. karnet, jednorazowe wejście
    double calculatePrice(int plannedVisits); //Ile student zapłaci za planowaną liczbę wejść
    double calculatePrice(); //Ile student zapłaci za planowaną liczbę wejść
}
