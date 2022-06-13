package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;

public interface UserBankAccountService {

    UserBankAccount saveUserBankAccount(UserBankAccount userBankAccount);

    UserBankAccount findUserBankAccountById(int id);

    User findUserByUserBankAccountId(int id);

    UserBankAccount addUserToUserBankAccount(int userId, int bankAccountId);
}
