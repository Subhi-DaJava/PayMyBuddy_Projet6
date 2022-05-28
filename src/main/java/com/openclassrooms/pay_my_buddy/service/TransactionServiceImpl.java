package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.FeeApplication;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllTransactionByUser(int userId) {
        User userById = userRepository.findById(userId).orElse(null);
        if(userById == null){
            throw new UserNotExistingException("This userId ["+userId+"] doesn't exist yet !!");
        }
        if( userById != null){
            List<Transaction> transactions = userById.getTransactions();
            if (transactions.isEmpty()){
                return new ArrayList<>();
            }
            return transactions;
        }

        return null;
    }

    @Override
    public Transaction findTransactionById(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    /**
     *
     * @param userPayId User qui fait la transaction
     * @param userName User qui reçoit la somme
     * @param amount La somme transférée
     * @param description La description de transaction, le motif ou le détail
     */
    @Override
    @Transactional
    public void sendMoneyToBuddy(int userPayId, String userName, double amount, String description) {

        if (userPayId <=0 || userName == null || amount <= 0){
            return;
        }
        User contact = userRepository.findUserByUserName(userName);
        User userPayed = userRepository.findById(userPayId).orElse(null);

        Set<User> contacts = userPayed.getContacts();

        if(contacts.contains(contact) && userPayed.getBalance() > amount){

            contact.setBalance(contact.getBalance() + amount);
            userRepository.save(contact);

            double totalFeePayed = amount * FeeApplication.FEE;

            Transaction transaction = new Transaction();

            transaction.setUserPay(userPayed);
            transaction.setDateTransaction(LocalDate.now());
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setBuddyId(contact.getUserId());
            transaction.setTotalFeePayed(totalFeePayed);

            transactionRepository.save(transaction);

            List<Transaction> userPayedTransactions = userPayed.getTransactions();

            userPayedTransactions.add(transaction);

            userPayed.setTransactions(userPayedTransactions);
            userPayed.setBalance(userPayed.getBalance() - amount - totalFeePayed);
            userRepository.save(userPayed);
        }
        else {
            throw new RuntimeException("This userName is not found +"+userName);
        }


    }
}
