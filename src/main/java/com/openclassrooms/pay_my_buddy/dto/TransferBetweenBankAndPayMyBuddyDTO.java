package com.openclassrooms.pay_my_buddy.dto;

import com.openclassrooms.pay_my_buddy.constant.OperationType;

import java.time.LocalDate;

public class TransferBetweenBankAndPayMyBuddyDTO {

    private String sourceUserBanAccountName;
    private String destinationPMBUserName;
    private double amount;
    private String description;
    private OperationType operationType;
    private LocalDate dateTransfer;

    public TransferBetweenBankAndPayMyBuddyDTO() {
    }

    public TransferBetweenBankAndPayMyBuddyDTO(String sourceUserBanAccountName, String destinationPMBUserName, double amount, String description, OperationType operationType, LocalDate dateTransfer) {
        this.sourceUserBanAccountName = sourceUserBanAccountName;
        this.destinationPMBUserName = destinationPMBUserName;
        this.amount = amount;
        this.description = description;
        this.operationType = operationType;
        this.dateTransfer = dateTransfer;
    }

    public String getSourceUserBanAccountName() {
        return sourceUserBanAccountName;
    }

    public void setSourceUserBanAccountName(String sourceUserBanAccountName) {
        this.sourceUserBanAccountName = sourceUserBanAccountName;
    }

    public String getDestinationPMBUserName() {
        return destinationPMBUserName;
    }

    public void setDestinationPMBUserName(String destinationPMBUserName) {
        this.destinationPMBUserName = destinationPMBUserName;
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

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDate getDateTransfer() {
        return dateTransfer;
    }

    public void setDateTransfer(LocalDate dateTransfer) {
        this.dateTransfer = dateTransfer;
    }
}
