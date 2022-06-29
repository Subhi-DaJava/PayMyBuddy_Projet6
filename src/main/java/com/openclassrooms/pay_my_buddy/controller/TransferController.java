package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.exception.TransferNotExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping("/bank-transfer/to-pay-my-buddy")
    public String transferMoneyToPayMyBuddyUser(
            @RequestParam(name = "codeIBAN") String codeIBAN,
            @ModelAttribute("userEmail") String userEmail,
            @RequestParam(name = "amount") double amount,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "operationType") OperationType operationType,
            @RequestParam(defaultValue = "0") int page) {
        logger.debug("This transferMoneyToPayMyBuddyUser methode starts here.(TransferController)");


        if (userEmail == null || codeIBAN == null || amount <= 0) {
            logger.debug("UserEmail={}, codeIBAN={}, and amount={} should not null or empty", userEmail, codeIBAN, amount);

            throw new RuntimeException("Ban operation !(from transferMoneyToPayMyBuddy)");
        }
        userBankAccountService.sendMoneyToAppUser(codeIBAN, userEmail, amount, description, operationType);

        logger.info("This operation transfer money to PayMyBuddy is successful!(from transferMoneyToPayMyBuddy)");

        return "redirect:/transfer?page=" + page;
    }

    @GetMapping("/bank-transfer/to-pay-my-buddy")
    public String sendMoneyToPayMyBuddyUser(Model model,
                                            String codeIBAN,
                                            String amount,
                                            String description,
                                            OperationType operationType){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        model.addAttribute("userEmail", userEmail);
        model.addAttribute("codeIBAN", codeIBAN);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("operationType", operationType);

        return "send-money-to-PayMyBuddy";
    }



    @PostMapping("transfers/money-transfer-to-bankAccount")
    public ResponseEntity<Transfer> transferMoneyToUserBankAccount(
            @RequestParam Integer userId, @RequestParam Integer bankAccountId, @RequestParam double amount, @RequestParam String description) {
        if (bankAccountId <= 0 || userId <= 0 || amount <= 0) {
            return ResponseEntity.notFound().build();
        }
        Transfer transfer = transferService.transferMoneyToUserBankAccount(userId, bankAccountId, amount, description);
        if (transfer != null) {
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
