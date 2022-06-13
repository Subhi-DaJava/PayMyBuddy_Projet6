package com.openclassrooms.pay_my_buddy.dto;

public class TransactionDTO {
    private String userName;
    private String description;
    private double amount;

    public TransactionDTO() {
    }

    public TransactionDTO(String userName, String description, double amount) {
        this.userName = userName;
        this.description = description;
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
