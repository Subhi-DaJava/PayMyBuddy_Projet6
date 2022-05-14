package com.openclassrooms.pay_my_buddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Table(name = "transaction")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double amount;

    private String description;

    @Column(name = "local_date")
    private LocalDate localDate;

    @ManyToOne
    private User userPayed;

    public Transaction() {
    }

    public Transaction(int id, double amount, String description, LocalDate localDate, User userPayed) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.localDate = localDate;
        this.userPayed = userPayed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public User getUserPayed() {
        return userPayed;
    }

    public void setUserPayed(User userPayed) {
        this.userPayed = userPayed;
    }
}
