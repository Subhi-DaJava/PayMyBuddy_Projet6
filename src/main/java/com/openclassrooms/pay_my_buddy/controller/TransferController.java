package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.exception.TransferNotExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transfer;
import com.openclassrooms.pay_my_buddy.service.TransferService;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);
    @Autowired
    private TransferService transferService;
    @Autowired
    private UserBankAccountService userBankAccountService;



    @GetMapping("/transfers/{transId}")
    public ResponseEntity<Transfer> findByTransferId(@PathVariable Integer transId) {
        Transfer transfer = transferService.findTransferById(transId);
        if (transfer == null) {
            throw new TransferNotExistingException("This transId [" + transId + "] doesn't exist yet !!");
        } else {
            return ResponseEntity.ok().body(transfer);
        }
    }

    @GetMapping("/user-bank-account/transfers")
    public ResponseEntity<List<Transfer>> findAllTransfersByOneUserBankAccount(@RequestParam Integer bank_account_Id) {
        List<Transfer> transfers = transferService.findAllTransfersByOneUserBankAccountId(bank_account_Id);
        if (transfers == null) {
            throw new UserNotExistingException("This bank_account_id [" + bank_account_Id + "] doesn't exist yet !!");
        } else if (transfers.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>());
        } else {
            return ResponseEntity.ok().body(transfers);
        }
    }

    @PostMapping("/transfer/pmb-bank")
    public String transferMoneyToPayMyBuddyUser(
            @RequestParam(name = "amount") double amount,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "operationType") OperationType operationType,
            @RequestParam(defaultValue = "0") int page) {
        logger.debug("This transferMoneyToPayMyBuddyUser methode starts here.(TransferController)");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();


        userBankAccountService.transferBetweenBankAnaPMB(userEmail, amount, description, operationType);

        logger.info("This operation transfer money to PayMyBuddy is successful!(from transferMoneyToPayMyBuddy)");

        return "redirect:/transfer?page=" + page;
    }

    @GetMapping("/transfer/pmb-bank")
    public String transferBetweenBankAndPMB(Model model,
                                            String amount,
                                            String description,
                                            OperationType operationType){

        String BANKtoPMB = String.valueOf(OperationType.CREDIT);
        String PMBtoBANK = String.valueOf(OperationType.DEBIT);
        List<String> operationTypes = new ArrayList<>();
        operationTypes.add(BANKtoPMB);
        operationTypes.add(PMBtoBANK);

        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("operationType", operationType);
        model.addAttribute("operationTypes", operationTypes);


        return "transfersBetweenBankAndPMB";
    }

}
