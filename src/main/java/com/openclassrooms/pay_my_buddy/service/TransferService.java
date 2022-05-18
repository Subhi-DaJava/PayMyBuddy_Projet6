package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.Transfer;

import java.util.List;

public interface TransferService {

    Transfer saveTransfer(Transfer transfer);
    Transfer findTransferById(int id);

    List<Transfer> findAllTransfersByOneUserBankAccountId(int id);

    Transfer transferMoneyToPayMyBuddyUser(int userBankId, int userId, double amount, String description);
    Transfer transferMoneyToUserBankAccount(int userId, int userBankId, double amount, String description);


}
