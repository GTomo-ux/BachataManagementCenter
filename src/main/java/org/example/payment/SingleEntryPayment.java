package org.example.payment;

public class SingleEntryPayment implements PaymentStrategy{

    private final double pricePerVisit;

    public SingleEntryPayment() {
        this.pricePerVisit = 30;
    }

    @Override
    public String getName() {
        return "Single Entry";
    }

    @Override
    public double calculatePrice(int plannedVisits) {
        return this.pricePerVisit * plannedVisits;
    }

    @Override
    public double calculatePrice() {
        return this.pricePerVisit;
    }
}
