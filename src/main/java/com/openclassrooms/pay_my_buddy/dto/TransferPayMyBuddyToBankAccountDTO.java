package com.openclassrooms.pay_my_buddy.dto;

import com.openclassrooms.pay_my_buddy.constant.OperationType;

import java.time.LocalDate;

public class TransferPayMyBuddyToBankAccountDTO {
    private String sourcePayMyBuddyUserName;
    private String destinationBankAccountUserName;
    private OperationType operationType;
    private LocalDate dateTransfer;
}
