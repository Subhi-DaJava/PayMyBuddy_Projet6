package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);

    List<Transaction> findAllTransactionByUser(User user);

    Transaction findTransactionById(int id);

    void sendMoney(int userPayId, String userName, double amount, String description);

    Transaction updateTransaction(int id, Transaction transaction);

}
