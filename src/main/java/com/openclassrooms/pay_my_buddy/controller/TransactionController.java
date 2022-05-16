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
    public void sendMoney(@RequestParam int payedId, @RequestParam String userName, @RequestParam double amount, @RequestParam String description){
        //payedId null, suerName null, amount 0, -, description null or no
        transactionService.sendMoney(payedId,userName,amount,description);
    }

    @PostMapping("/transactions/{id}")
    public Transaction updateTransaction(@PathVariable int id, @RequestBody Transaction transaction){
        return transactionService.updateTransaction(id,transaction);
    }

    @GetMapping("/transactions/user/{userId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByUser(@PathVariable Integer userId){
        List<Transaction> transactionsByUser = transactionService.findAllTransactionByUser(userId);

        if (transactionsByUser.isEmpty()){
            List<Transaction> transactionsEmpty = new ArrayList<>();
            return ResponseEntity.ok().body(transactionsEmpty);
        }
        else if(transactionsByUser == null){
            throw new UserNotExistingException("This userId : "+userId+" doesn't exist DB !");
        }
        else
            return ResponseEntity.ok().body(transactionsByUser);
    }


}
