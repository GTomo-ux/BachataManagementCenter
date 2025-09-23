package org.example.payment;

public class MonthlyUnlimitedPayment implements PaymentStrategy{

    public final double monthlyPrice;

    public MonthlyUnlimitedPayment() {
        this.monthlyPrice = 200;
    }

    @Override
    public String getName() {
        return "Monthly Unlimited";
    }

    @Override
    public double calculatePrice(int plannedVisits) {
        return this.monthlyPrice;
    }

    @Override
    public double calculatePrice() {
        return this.monthlyPrice;
    }
}
