package com.openclassrooms.pay_my_buddy.model;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Transactional
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transId;

    private double amount;

    private String description;

    @Column(name = "transaction_date")
    private LocalDate dateTransaction;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User userPay;

    @Column(name = "total_fee_payed")
    private double totalFeePayed;

    @Column(name = "recipient_id")
    private int buddyId;

    public Transaction() {
    }

    public Transaction(int transId, double amount, String description, LocalDate dateTransaction, User userPay, double totalFeePayed, int buddyId) {
        this.transId = transId;
        this.amount = amount;
        this.description = description;
        this.dateTransaction = dateTransaction;
        this.userPay = userPay;
        this.totalFeePayed = totalFeePayed;
        this.buddyId = buddyId;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
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

    public double getTotalFeePayed() {
        return totalFeePayed;
    }

    public void setTotalFeePayed(double totalFeePayed) {
        this.totalFeePayed = totalFeePayed;
    }

    public int getBuddyId() {
        return buddyId;
    }

    public void setBuddyId(int buddyId) {
        this.buddyId = buddyId;
    }
}
