package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);
    List<Transaction> findAllTransactionByUser(User user);
    Transaction findTransactionById(int id);
    void addTransactionToUser(int userId, int transactionId);

    void sendMoney(int userPayId, int userRecipientId, double amount);

}
