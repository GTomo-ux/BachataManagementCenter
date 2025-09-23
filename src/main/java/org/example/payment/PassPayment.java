package org.example.payment;

public class PassPayment implements PaymentStrategy{

    private final int numberOfEntries;
    private final double pricePerEntry;

    public PassPayment(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
        this.pricePerEntry = 25;
    }

    @Override
    public String getName() {
        return "Pass for " + numberOfEntries + " entries.";
    }

    @Override
    public double calculatePrice(int plannedVisits) {


        return this.pricePerEntry * this.numberOfEntries;
    }

    @Override
    public double calculatePrice() {
        return this.pricePerEntry * this.numberOfEntries;
    }
}
