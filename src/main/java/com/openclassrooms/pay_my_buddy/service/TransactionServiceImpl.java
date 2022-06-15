package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.FeeApplication;
import com.openclassrooms.pay_my_buddy.dto.TransactionDTO;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
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
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> findAllTransactionByUser(String userEmail) {
        logger.debug("This findAllTransactionByUser starts here");
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        User userByEmail = userRepository.findUserByEmail(userEmail);
        if (userByEmail == null) {
            logger.debug("UserEmail={} should not be null", userEmail);
            throw new UserNotExistingException("This userEmail [" + userEmail + "] doesn't exist yet !!");
        }
        if (userByEmail != null) {
            List<Transaction> transactions = userByEmail.getTransactions();
            for (Transaction transaction : transactions) {
                /*
                  Chercher le buddyEmail pour trouver le contact
                 */
                String userBuddyEmail = transaction.getBuddyEmail();
                User userContact = userRepository.findUserByEmail(userBuddyEmail);
                TransactionDTO transactionDTO = new TransactionDTO(userContact.getFirstName() + "   "+userContact.getLastName(), transaction.getDescription(), transaction.getAmount());
                transactionDTOList.add(transactionDTO);
            }

            if (transactions.isEmpty()) {
                return new ArrayList<>();
            }

            return transactionDTOList;
        }

        return null;
    }


    @Override
    @Transactional
    public void sendMoneyToBuddy(String userEmail, String userContactEmail, double amount, String description) {
        logger.debug("This methode sendMoneyToBuddy starts here");

        if (userEmail == null || userEmail == null || amount <= 0) {
            return;
        }
        User contact = userRepository.findUserByEmail(userContactEmail);

        User userPayed = userRepository.findUserByEmail(userEmail);

        Set<User> contacts = userPayed.getContacts();

        if (contact == null) {
            logger.debug("Connection email={} doesn't exist in the DB" + userContactEmail);
            throw new UserNotExistingException("This user with email={} doesn't exist" + userContactEmail);
        }

        if (contacts.contains(contact) && userPayed.getBalance() > amount) {

            contact.setBalance(contact.getBalance() + amount);
            userRepository.save(contact);

            double totalFeePayed = amount * FeeApplication.FEE;

            Transaction transaction = new Transaction();

            transaction.setUserPay(userPayed);
            transaction.setDateTransaction(LocalDate.now());
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setBuddyEmail(contact.getEmail());
            transaction.setTotalFeePayed(totalFeePayed);

            transactionRepository.save(transaction);

            List<Transaction> userPayedTransactions = userPayed.getTransactions();

            userPayedTransactions.add(transaction);

            userPayed.setTransactions(userPayedTransactions);
            userPayed.setBalance(userPayed.getBalance() - amount - totalFeePayed);
            userRepository.save(userPayed);

        }

/*    @Override
    public Page<Transaction> findTransactionDTO(User user, Pageable pageable) {

        Page<Transaction> transactionsDTO = transactionRepository.findTransactionByUserPay(user, pageable);

        Page<TransactionDTO> transactionDTOS;


        return null;
    }*/

    }
}
