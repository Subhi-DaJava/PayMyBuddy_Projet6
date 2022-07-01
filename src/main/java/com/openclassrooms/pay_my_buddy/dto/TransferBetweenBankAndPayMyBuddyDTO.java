package com.openclassrooms.pay_my_buddy.dto;

import com.openclassrooms.pay_my_buddy.constant.OperationType;

import java.time.LocalDate;

public class TransferBetweenBankAndPayMyBuddyDTO {

    private String sourceAccountUserName;
    private String destinationUserName;
    private OperationType operationType;
    private LocalDate dateTransfer;

}
