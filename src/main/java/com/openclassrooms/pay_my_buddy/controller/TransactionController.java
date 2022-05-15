package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions/send_money")
    public void sendMoney(@RequestParam int payedId, @RequestParam String userName, @RequestParam double amount, @RequestParam String description){
        transactionService.sendMoney(payedId,userName,amount,description);
    }

    @PostMapping("/transactions")
    public Transaction saveTransaction(@RequestBody Transaction transaction){
        return transactionService.addTransaction(transaction);
    }

    @PostMapping("/transactions/{id}")
    public Transaction updateTransaction(@PathVariable int id, @RequestBody Transaction transaction){
        return transactionService.updateTransaction(id,transaction);
    }


}
