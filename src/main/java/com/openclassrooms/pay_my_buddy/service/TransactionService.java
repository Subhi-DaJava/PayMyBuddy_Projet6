package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);

    List<Transaction> findAllTransactionByUser(int userId);

    Transaction findTransactionById(int id);

    void sendMoneyToBuddy(int userPayId, String userName, double amount, String description);


}
