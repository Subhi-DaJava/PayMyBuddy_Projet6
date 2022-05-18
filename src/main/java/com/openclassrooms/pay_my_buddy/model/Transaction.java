package com.openclassrooms.pay_my_buddy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Column(name = "id")
    private int transId;

    private double amount;

    private String description;

    @Column(name = "local_date")
    private LocalDate dateTransaction;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User userPay;

    @Column(name = "recipient_name")
    private String buddyName;

    @Column(name="buddy_email")
    private String buddyEmail;

    public Transaction() {
    }

    public Transaction(double amount, String description, LocalDate dateTransaction, User userPay, String buddyName, String buddyEmail) {
        this.amount = amount;
        this.description = description;
        this.dateTransaction = dateTransaction;
        this.userPay = userPay;
        this.buddyName = buddyName;
        this.buddyEmail = buddyEmail;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int id) {
        this.transId = id;
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

    public String getBuddyName() {
        return buddyName;
    }

    public void setBuddyName(String buddyName) {
        this.buddyName = buddyName;
    }

    public String getBuddyEmail() {
        return buddyEmail;
    }

    public void setBuddyEmail(String buddyEmail) {
        this.buddyEmail = buddyEmail;
    }
}
