package com.openclassrooms.pay_my_buddy.dto;

import java.time.LocalDate;

public class TransactionDTO {
    private String buddyName;
    private String description;
    private double amount;

    private LocalDate dateTransaction;

    public TransactionDTO() {
    }

}
