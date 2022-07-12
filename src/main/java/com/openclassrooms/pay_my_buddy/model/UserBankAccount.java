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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private int bankAccountId;

    @NotBlank
    @Column(name = "bank_name", length = 25)
    private String bankName;

    @Column(name = "bank_location", length = 45)
    private String bankLocation;

    @Column(name = "code_iban", unique = true, length = 100)
    @NotBlank
    private String codeIBAN;

    @NotBlank
    @Column(name = "code_bic", length = 45)
    private String codeBIC;

    @OneToOne
    private AppUser appUser;

    @OneToMany(mappedBy = "userBankAccount")
    private List<Transfer> transfers = new ArrayList<>();

    public UserBankAccount() {
    }

    public UserBankAccount(int bankAccountId,
                           String bankName,
                           String bankLocation,
                           String codeIBAN,
                           String codeBIC, AppUser appUser, List<Transfer> transfers) {
        this.bankAccountId = bankAccountId;
        this.bankName = bankName;
        this.bankLocation = bankLocation;
        this.codeIBAN = codeIBAN;
        this.codeBIC = codeBIC;
        this.appUser = appUser;
        this.transfers = transfers;
    }

    public UserBankAccount(String bankName,
                           String bankLocation,
                           String codeIBAN,
                           String codeBIC,
                           AppUser appUser) {
        this.bankName = bankName;
        this.bankLocation = bankLocation;
        this.codeIBAN = codeIBAN;
        this.codeBIC = codeBIC;
        this.appUser = appUser;
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }
}
