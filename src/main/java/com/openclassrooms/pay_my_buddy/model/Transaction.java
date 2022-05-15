package com.openclassrooms.pay_my_buddy.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Transactional
@Table(name = "transaction")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double amount;

    private String description;

    @Column(name = "local_date")
    private LocalDate dateTransaction;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User userPay;

    public Transaction() {
    }

    public Transaction(int id, double amount, String description, LocalDate dateTransaction, User userPay) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.dateTransaction = dateTransaction;
        this.userPay = userPay;
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

    public LocalDate getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDate dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public User getUserPay() {
        return userPay;
    }

    public void setUserPay(User userPay) {
        this.userPay = userPay;
    }
}
