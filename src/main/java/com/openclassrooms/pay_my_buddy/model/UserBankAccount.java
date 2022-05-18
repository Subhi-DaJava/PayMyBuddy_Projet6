package com.openclassrooms.pay_my_buddy.model;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_bank_account")
@Transactional
public class UserBankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bankAccountId;

    private double balance;
    @Column(name = "account_name",unique = true, length = 100)
    private String accountName;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "userBankAccount")
    private List<Transfer> transfers = new ArrayList<>();


    public UserBankAccount() {
    }

    public UserBankAccount(double balance, String accountName, User user, List<Transfer> transfers) {
        this.balance = balance;
        this.accountName = accountName;
        this.user = user;
        this.transfers = transfers;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBankAccount that = (UserBankAccount) o;
        return bankAccountId == that.bankAccountId && Double.compare(that.balance, balance) == 0 && Objects.equals(user, that.user) && Objects.equals(transfers, that.transfers);
    }

}
