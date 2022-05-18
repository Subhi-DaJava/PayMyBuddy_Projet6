package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public void sendMoney(int userPayId, String userName, double amount, String description) {
        if (userPayId <=0 || userName == null || amount <= 0){
            return;
        }
        User userPayed = userRepository.findById(userPayId).orElse(null);
        Set<User> contacts = userPayed.getContacts();
        for(User contact : contacts){
            //Attente à améliorer le code, if (userPayId.getBalance <= amount)
            if (contact.getUserName().equals(userName) && userPayed.getBalance() > amount){
                contact.setBalance(contact.getBalance() + amount);
                userRepository.save(contact);

                Transaction transaction = new Transaction();
                transaction.setUserPay(userPayed);
                transaction.setDateTransaction(LocalDate.now());
                transaction.setDescription(description);
                transaction.setAmount(amount);
                transaction.setBuddyName(userName);
                transaction.setBuddyEmail(contact.getEmail());
                Transaction transactionSaved = addTransaction(transaction);

                List<Transaction> userPayedTransactions = userPayed.getTransactions();
                userPayedTransactions.add(transaction);
                userPayed.setTransactions(userPayedTransactions);
                userPayed.setBalance(userPayed.getBalance() - amount);
                User userSaved = userRepository.save(userPayed);
                transactionSaved.setUserPay(userSaved);
                addTransaction(transactionSaved);


            } else
                throw new UserNotExistingException("User Not found or the balance is not enough");
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
