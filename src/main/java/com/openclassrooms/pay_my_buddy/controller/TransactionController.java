package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions/send_money")
    public void sendMoneyToBuddy(@RequestParam int payedId, @RequestParam String userName, @RequestParam double amount, @RequestParam String description){
        if (payedId <=0 || userName == null || amount <= 0){
            return;
        }
        transactionService.sendMoneyToBuddy(payedId,userName,amount,description);
    }


    @GetMapping("/transactions/user/{userId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByUser(@PathVariable Integer userId){
        List<Transaction> transactionsByUser = transactionService.findAllTransactionByUser(userId);

        if (transactionsByUser.isEmpty()){
            return ResponseEntity.ok().body(new ArrayList<>());
        }
        else if(transactionsByUser == null){
            throw new UserNotExistingException("This userId : "+userId+" doesn't exist DB !");
        }
        else
            return ResponseEntity.ok().body(transactionsByUser);
    }


}
