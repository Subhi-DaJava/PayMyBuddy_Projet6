package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
public class UserBankAccountController {
    private static final Logger logger = LoggerFactory.getLogger(UserBankAccountController.class);
    @Autowired
    private UserBankAccountService userBankAccountService;

    @PostMapping("/user-bank-accounts")
    public ResponseEntity<UserBankAccount> createUserBankAccount(@Valid @RequestBody UserBankAccount userBankAccount){
        UserBankAccount userBankAccountCreated = userBankAccountService.saveUserBankAccount(userBankAccount);

        if (userBankAccountCreated != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{bank_account_id}")
                    .buildAndExpand(userBankAccountCreated.getBankAccountId())
                    .toUri();
            return ResponseEntity.created(location).body(userBankAccountCreated);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user-bank-accounts/{accountId}")
    public ResponseEntity<UserBankAccount> findUserBankAccountById(@PathVariable Integer accountId){
        UserBankAccount userBankAccount = userBankAccountService.findUserBankAccountById(accountId);
        if (userBankAccount == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body(userBankAccount);
    }

    @GetMapping("/user-bank-accounts/users/accountId")
    public ResponseEntity<User> findUserByUserAccountId(@PathVariable Integer accountId){
        return ResponseEntity.ok().body(userBankAccountService.findUserByUserBankAccountId(accountId));
    }


    @PutMapping("/user-bank-accounts/users")
    public ResponseEntity<UserBankAccount> addUserToUserBankAccount(@RequestParam Integer userId, @RequestParam Integer bankAccountId){
        logger.debug("This methode starts here");
        UserBankAccount userBankAccount = userBankAccountService.addUserToUserBankAccount(userId, bankAccountId);

        if(userBankAccount == null){
            logger.debug("Not succeed, problems : maybe userId and bankAccountId already associated or the Ids don't exist !!!");
            throw new UserNotExistingException("This userId ["+userId+"], or that bankAccountId ["+bankAccountId+" doesn't exist !!");
        }
        return ResponseEntity.ok().body(userBankAccount);
    }
}
