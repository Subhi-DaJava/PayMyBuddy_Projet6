package com.openclassrooms.pay_my_buddy.dto;

import java.time.LocalDate;

public class Payment {

    private String getPayedName;
    private String email;
    private LocalDate localDate;
    private double amont;
    private String description;

    public Payment() {
    }

    public Payment(String getPayedName, String email, LocalDate localDate, double amont, String userBankName) {
        this.getPayedName = getPayedName;
        this.email = email;
        this.localDate = localDate;
        this.amont = amont;
        this.description = userBankName;
    }

    public String getGetPayedName() {
        return getPayedName;
    }

    public void setGetPayedName(String getPayedName) {
        this.getPayedName = getPayedName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public double getAmont() {
        return amont;
    }

    public void setAmont(double amont) {
        this.amont = amont;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
