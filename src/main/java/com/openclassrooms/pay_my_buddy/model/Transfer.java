package com.openclassrooms.pay_my_buddy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
@Entity
@Transactional
@Table(name = "transfer")
public class Transfer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transferId;

    private double debit;

    private double credit;

    private String description;

    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "bankAccountId", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserBankAccount userBankAccount;

    public Transfer() {
    }

    public Transfer(double debit, double credit, String description, LocalDate localDate, UserBankAccount userBankAccount) {
        this.debit = debit;
        this.credit = credit;
        this.description = description;
        this.localDate = localDate;
        this.userBankAccount = userBankAccount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public UserBankAccount getUserBankAccount() {
        return userBankAccount;
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
