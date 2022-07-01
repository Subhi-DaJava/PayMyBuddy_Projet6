package com.openclassrooms.pay_my_buddy.dto;

public class BankAccountDTO {
    private String bankName;
    private String codeIBAN;
    private String codeBIC;
    private String bankLocation;

    public BankAccountDTO() {
    }

    public BankAccountDTO(String bankName, String codeIBAN, String codeBIC, String bankLocation) {
        this.bankName = bankName;
        this.codeIBAN = codeIBAN;
        this.codeBIC = codeBIC;
        this.bankLocation = bankLocation;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(String bankLocation) {
        this.bankLocation = bankLocation;
    }
}
