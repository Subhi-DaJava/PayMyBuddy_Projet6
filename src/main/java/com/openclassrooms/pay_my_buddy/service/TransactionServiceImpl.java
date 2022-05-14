package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllTransactionByUser(User user) {
        if(userService.findUserByEmail(user.getEmail()) != null){
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
    public void addTransactionToUser(int userId, int transactionId) {
        User userById = userService.findUserById(userId);
        Transaction transactionById = transactionRepository.findById(transactionId).orElse(null);
        userById.getTransactions().add(transactionById);
    }

    @Override
    public void sendMoney(int userPayeId, int userRecipientId, double amount) {
        User userPayed = userService.findUserById(userPayeId);
        User userRecevedId = userService.findUserById(userRecipientId);
        if(userPayed.getBalance() > 0 && userPayed.getBalance() > amount){
            userRecevedId.setBalance(userRecevedId.getBalance()+amount);

            userService.saveUser(userRecevedId);
            userPayed.setBalance(userPayed.getBalance()-amount);
            userService.saveUser(userPayed);
        }
    }
}
