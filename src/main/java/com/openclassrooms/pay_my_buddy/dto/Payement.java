package com.openclassrooms.pay_my_buddy.dto;

public class Payement {
    private String destination_userName;
    private double amount;
    private String description;

    public Payement(String destination_userName, double amount, String description) {
        this.destination_userName = destination_userName;
        this.amount = amount;
        this.description = description;
    }

    public String getDestination_userName() {
        return destination_userName;
    }

    public void setDestination_userName(String destination_userName) {
        this.destination_userName = destination_userName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
