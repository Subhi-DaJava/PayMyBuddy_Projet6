package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.FeeApplication;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger("TransactionImpl.class");

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void sendMoneyToBuddy(String userEmail, String userContactEmail, double amount, String description) {
        logger.debug("This methode sendMoneyToBuddy starts here");

        if (userEmail == null || userEmail == null || amount <= 0) {
            return;
        }
        AppUser contact = userRepository.findByEmail(userContactEmail);

        AppUser appUserPayed = userRepository.findByEmail(userEmail);

        Set<AppUser> contacts = appUserPayed.getContacts();

        if (contact == null) {
            logger.debug("Connection email={} doesn't exist in the DB" + userContactEmail);
            throw new UserNotExistingException("This user with email={} doesn't exist" + userContactEmail);
        }

        if (contacts.contains(contact) && appUserPayed.getBalance() > amount) {

            contact.setBalance(contact.getBalance() + amount);
            userRepository.save(contact);

            double totalFeePayed = amount * FeeApplication.FEE;

            Transaction transaction = new Transaction();

            transaction.setSource(appUserPayed);
            transaction.setDateTransaction(LocalDate.now());
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setTarget(contact);
            transaction.setTotalFeePayed(totalFeePayed);

            transactionRepository.save(transaction);

            List<Transaction> userPayedTransactions = appUserPayed.getTransactionsSources();

            userPayedTransactions.add(transaction);

            appUserPayed.setTransactionsSources(userPayedTransactions);
            appUserPayed.setBalance(appUserPayed.getBalance() - amount - totalFeePayed);
            userRepository.save(appUserPayed);

        }

    }
}
