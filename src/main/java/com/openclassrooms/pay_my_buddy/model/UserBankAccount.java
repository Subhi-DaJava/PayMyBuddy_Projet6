package com.openclassrooms.pay_my_buddy.model;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_bank_account")
@Transactional
public class UserBankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private int bankAccountId;

    @NotBlank
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_location")
    private String bankLocation;

    @Column(name = "code_iban", unique = true, length = 100)
    @NotBlank
    private String codeIBAN;

    @NotBlank
    @Column(name = "code_bic", unique = true, length = 100)
    private String codeBIC;

    private double balance;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "userBankAccount")
    private List<Transfer> transfers = new ArrayList<>();

    public UserBankAccount() {
    }

    public UserBankAccount(int bankAccountId, String bankName, String bankLocation, String codeIBAN, String codeBIC, double balance, User user, List<Transfer> transfers) {
        this.bankAccountId = bankAccountId;
        this.bankName = bankName;
        this.bankLocation = bankLocation;
        this.codeIBAN = codeIBAN;
        this.codeBIC = codeBIC;
        this.balance = balance;
        this.user = user;
        this.transfers = transfers;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(String bankLocation) {
        this.bankLocation = bankLocation;
    }

    public String getCodeIBAN() {
        return codeIBAN;
    }

    public void setCodeIBAN(String codeIBAN) {
        this.codeIBAN = codeIBAN;
    }

    public String getCodeBIC() {
        return codeBIC;
    }

    public void setCodeBIC(String codeBIC) {
        this.codeBIC = codeBIC;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }
}
