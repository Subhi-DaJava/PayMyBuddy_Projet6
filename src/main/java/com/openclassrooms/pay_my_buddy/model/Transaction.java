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
    private int id;

    private double amount;

    private String description;

    @Column(name = "transaction_date")
    private LocalDate dateTransaction;

    @OneToOne
    @JoinColumn(name = "source")
    private AppUser source;

    @Column(name = "total_fee_payed")
    private double totalFeePayed;


    @OneToOne
    @JoinColumn(name ="target")
    private AppUser target;

    public Transaction() {
    }

    public Transaction(int id, double amount, String description, LocalDate dateTransaction, AppUser source, double totalFeePayed, AppUser target) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.dateTransaction = dateTransaction;
        this.source = source;
        this.totalFeePayed = totalFeePayed;
        this.target = target;
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

    public AppUser getSource() {
        return source;
    }

    public void setSource(AppUser source) {
        this.source = source;
    }

    public double getTotalFeePayed() {
        return totalFeePayed;
    }

    public void setTotalFeePayed(double totalFeePayed) {
        this.totalFeePayed = totalFeePayed;
    }

    public AppUser getTarget() {
        return target;
    }

    public void setTarget(AppUser target) {
        this.target = target;
    }
}
