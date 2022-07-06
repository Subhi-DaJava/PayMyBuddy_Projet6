package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.dto.BankAccountDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;

public interface UserBankAccountService {

     void addBankAccountToPayMyBuddy(String userEmail,
                                                    String bankName,
                                                    String bankLocation,
                                                    String codeIBAN,
                                                    String codeBIC);

    UserBankAccount findUserBankAccountById(int id);

    void transferBetweenBankAndPMB(String userEmail,
                                   double amount,
                                   String description,
                                   OperationType operationType);

    BankAccountDTO bankAccountInfo(AppUser appUser);

    UserBankAccount findByCodeIBAN(String codeIBAN);
}
