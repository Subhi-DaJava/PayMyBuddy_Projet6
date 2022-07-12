package com.openclassrooms.pay_my_buddy.model;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Transactional
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private int transferId;

    @Column(name = "operation_type", length = 15)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private double amount;

    private String description;

    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "bankAccountId")
    private UserBankAccount userBankAccount;

    public Transfer() {
    }

    public Transfer(int transferId, OperationType operationType, double amount, String description, LocalDate transactionDate, UserBankAccount userBankAccount) {
        this.transferId = transferId;
        this.operationType = operationType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.userBankAccount = userBankAccount;
    }

    public Transfer(OperationType operationType, double amount, String description, LocalDate transactionDate, UserBankAccount userBankAccount) {
        this.operationType = operationType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.userBankAccount = userBankAccount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public UserBankAccount getUserBankAccount() {
        return userBankAccount;
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }
}
