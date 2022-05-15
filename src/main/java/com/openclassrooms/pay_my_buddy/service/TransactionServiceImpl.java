package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
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
    public List<Transaction> findAllTransactionByUser(User user) {
        if(userRepository.findUserByEmail(user.getEmail()) != null){
            List<Transaction> transactions = user.getTransactions();
            return transactions;
        }
        return null;
    }

    @Override
    public Transaction findTransactionById(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void sendMoney(int userPayId, String userName, double amount, String description) {
        User userPayed = userRepository.findById(userPayId).orElse(null);
        Set<User> contacts = userPayed.getContacts();
        for(User contact : contacts){

            if (contact.getUserName().equals(userName) && userPayed.getBalance() > amount){
                contact.setBalance(contact.getBalance() + amount);
                userRepository.save(contact);

                Transaction transaction = new Transaction();
                transaction.setUserPay(userPayed);
                transaction.setDateTransaction(LocalDate.now());
                transaction.setDescription(description);
                transaction.setAmount(amount);
                Transaction transactionSaved = addTransaction(transaction);

                List<Transaction> userPayedTransactions = userPayed.getTransactions();
                userPayedTransactions.add(transaction);
                userPayed.setTransactions(userPayedTransactions);
                userPayed.setBalance(userPayed.getBalance() - amount);
                User userSaved = userRepository.save(userPayed);
                transactionSaved.setUserPay(userSaved);
                addTransaction(transactionSaved);


            } else
                throw new RuntimeException("Not found or the balance is not enough");
        }
    }

    @Override
    public Transaction updateTransaction(int id,Transaction transaction) {
        Transaction transactionUpdating = findTransactionById(id);
        transactionUpdating.setAmount(transaction.getAmount());
        transactionUpdating.setUserPay(transaction.getUserPay());
        transactionUpdating.setDescription(transaction.getDescription());
        transactionUpdating.setDateTransaction(transaction.getDateTransaction());

        return transactionRepository.save(transactionUpdating);
    }


}
