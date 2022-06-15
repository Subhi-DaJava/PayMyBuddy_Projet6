package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.dto.TransactionDTO;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);

    List<TransactionDTO> findAllTransactionByUser(String userEmail);


    void sendMoneyToBuddy(String userEmail, String contactEmail, double amount, String description);

    /*Page<Transaction> findTransactionDTO(User user, Pageable pageable);*/

}
