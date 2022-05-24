package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.TransferNotExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transfer;
import com.openclassrooms.pay_my_buddy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/transfers/{transId}")
    public ResponseEntity<Transfer> findByTransferId(@PathVariable Integer transId){
        Transfer transfer = transferService.findTransferById(transId);
        if (transfer == null)
            throw new TransferNotExistingException("This transId ["+transId+ "] doesn't exist yet !!");
        else
            return ResponseEntity.ok().body(transfer);
    }

    @GetMapping("/user-bank-account/transfers")
    public ResponseEntity<List<Transfer>> findAllTransfersByOneUserBankAccount(@RequestParam Integer bank_account_Id){
        List<Transfer> transfers = transferService.findAllTransfersByOneUserBankAccountId(bank_account_Id);
        if(transfers == null )
            throw new UserNotExistingException("This bank_account_id ["+bank_account_Id+"] doesn't exist yet !!");

        else if (transfers.isEmpty()){
            return ResponseEntity.ok().body(new ArrayList<>());
        }else
            return ResponseEntity.ok().body(transfers);
    }

    @PostMapping("/transfers/money-transfer/pay-my-buddy")
    public ResponseEntity<Transfer> transferMoneyToPayMyBuddyUser(
            @RequestParam Integer bankAccountId, @RequestParam Integer userId, @RequestParam double amount, @RequestParam String description){
        if (bankAccountId <= 0 || userId <= 0 || amount <= 0){
            return ResponseEntity.notFound().build();
        }

        Transfer transfer = transferService.transferMoneyToPayMyBuddyUser(bankAccountId,userId,amount,description);
        if(transfer != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/transferId")
                    .buildAndExpand(transfer.getTransferId())
                    .toUri();
            return ResponseEntity.created(location).body(transfer);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("transfers/money-transfer-to-bankAccount")
    public ResponseEntity<Transfer> transferMoneyToUserBankAccount(
            @RequestParam Integer userId, @RequestParam Integer bankAccountId, @RequestParam double amount, @RequestParam String description){
        if (bankAccountId <= 0 || userId <= 0 || amount <= 0){
            return ResponseEntity.notFound().build();
        }
        Transfer transfer = transferService.transferMoneyToUserBankAccount(userId,bankAccountId,amount,description);
        if(transfer != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/transferId")
                    .buildAndExpand(transfer.getTransferId())
                    .toUri();
            return ResponseEntity.created(location).body(transfer);
        }
        return ResponseEntity.notFound().build();
    }

}
