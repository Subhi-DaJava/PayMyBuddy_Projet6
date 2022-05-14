package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PutMapping("/transactions/{payedId}/{recipientId}/{amount}")
    public void sendMoney(@PathVariable int payedId, @PathVariable int recipientId, @PathVariable double amount){
        transactionService.sendMoney(payedId,recipientId,amount);
    }


}
