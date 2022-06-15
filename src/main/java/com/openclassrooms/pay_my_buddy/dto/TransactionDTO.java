package com.openclassrooms.pay_my_buddy.dto;

public class TransactionDTO {
    private String buddyName;
    private String description;
    private double amount;

    public TransactionDTO() {
    }

    public TransactionDTO(String userName, String description, double amount) {
        this.buddyName = userName;
        this.description = description;
        this.amount = amount;
    }

    public String getBuddyName() {
        return buddyName;
    }

    public void setBuddyName(String buddyName) {
        this.buddyName = buddyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
