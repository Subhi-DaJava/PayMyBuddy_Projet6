package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.dto.Payment;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transaction;

import java.util.List;

public interface TransactionService {

    void sendMoneyToBuddy(String userEmail, String contactEmail, double amount, String description);

    List<Payment> findTransactionsBySource(AppUser appUser);

}
