package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;

public interface UserBankAccountService {

    void addBankAccountToPayMyBuddy(AppUser appUser,
                                    String bankName,
                                    String bankLocation,
                                    String codeIBAN,
                                    String codeBIC);

    UserBankAccount findUserBankAccountById(int id);

    AppUser findUserByUserBankAccountId(int id);

    UserBankAccount addUserToUserBankAccount(int userId, int bankAccountId);

    void sendMoneyToAppUser(String codeIBAN,
                            String userEmail,
                            double amount,
                            String description,
                            OperationType operationType);

    UserBankAccount findUserBankAccountByCodeIBAN(String codeIBAN);

}
