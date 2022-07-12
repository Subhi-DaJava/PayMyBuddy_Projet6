package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.FeeApplication;
import com.openclassrooms.pay_my_buddy.dto.Payment;
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

import java.util.ArrayList;
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
    public void sendMoneyToBuddy(String userEmail, String buddyEmail, double amount, String description) {
        logger.debug("This methode sendMoneyToBuddy starts here");

        if (userEmail == null || buddyEmail == null || amount <= 0) {
            throw new RuntimeException("userEmail," +
                    " buddyEmail should not be null and amount should be greater than 0 !(from TransactionServiceImpl)");
        }
        AppUser connection = userRepository.findByEmail(buddyEmail);

        AppUser appUserPayed = userRepository.findByEmail(userEmail);

        Set<AppUser> connections = appUserPayed.getConnections();

        if (connection == null) {
            logger.debug("Connection email={} doesn't exist in the DB" + buddyEmail);
            throw new UserNotExistingException("This user with email={} doesn't exist" + buddyEmail);
        }

        if (connections.contains(connection) && appUserPayed.getBalance() > amount) {

            connection.setBalance(connection.getBalance() + amount);
            userRepository.save(connection);

            double totalFeePayed = amount * FeeApplication.FEE;

            Transaction transaction = new Transaction();

            transaction.setSource(appUserPayed);
            transaction.setDateTransaction(LocalDate.now());
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setTarget(connection);
            transaction.setTotalFeePayed(totalFeePayed);

            transactionRepository.save(transaction);

            List<Transaction> userPayedTransactions = appUserPayed.getTransactionsSources();

            userPayedTransactions.add(transaction);

            appUserPayed.setTransactionsSources(userPayedTransactions);
            appUserPayed.setBalance(appUserPayed.getBalance() - amount - totalFeePayed);
            userRepository.save(appUserPayed);

        }

    }

    @Override
    public List<Payment> findTransactionsBySource(AppUser appUser) {
        List<Transaction> transactions = transactionRepository.findBySource(appUser);
        List<Payment> payments = new ArrayList<>();

        Payment payment;

        for(Transaction transaction : transactions){
            payment = new Payment();

            payment.setGetPayedName(transaction.getTarget().getFirstName() + " " + transaction.getTarget().getLastName());
            payment.setEmail(transaction.getTarget().getEmail());
            payment.setAmont(transaction.getAmount());
            payment.setLocalDate(transaction.getDateTransaction());
            payment.setDescription(transaction.getDescription());

            payments.add(payment);
        }

        return payments;
    }
}
