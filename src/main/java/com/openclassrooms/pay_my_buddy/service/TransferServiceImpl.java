package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.exception.TransferNotExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.Transfer;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.repository.TransferRepository;
import com.openclassrooms.pay_my_buddy.repository.UserBankAccountRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserBankAccountRepository userBankAccountRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Transfer saveTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public Transfer findTransferById(int id) {
        Transfer transfer = transferRepository.findById(id).orElse(null);
        if (transfer == null) {
            throw new TransferNotExistingException("This id [" + id + "] doesn't exist yet !!");
        } else {
            return transfer;
        }
    }

    @Override
    public List<Transfer> findAllTransfersByOneUserBankAccountId(int id) {
        UserBankAccount userBankAccount = userBankAccountRepository.findById(id).orElse(null);
        if (userBankAccount == null) {
            throw new UserNotExistingException("This userBankAccount [" + id + "] doesn't exist yet !!");
        }
        List<Transfer> transfersByUserAccountId = userBankAccount.getTransfers();
        if (transfersByUserAccountId.isEmpty()) {
            return new ArrayList<>();
        } else {
            return transfersByUserAccountId;
        }
    }

    @Override
    public Transfer transferMoneyToPayMyBuddyUser(String userEmail, String buddyEmail, double amount, String description) {

        User userPay = userRepository.findUserByEmail(userEmail);
        User userBuddy = userRepository.findUserByEmail(buddyEmail);

   /*     if (userEmail == null || buddyEmail == null || amount <= 0 || userPay.getBalance() < amount) {
            return null;
        }
        UserBankAccount userBankAccount = userBankAccountRepository.findById(userBankId).orElse(null);

        User user = userRepository.findById(userId).orElse(null);

        userBankAccount.setBalance(userBankAccount.getBalance() - amount);
        userBankAccountRepository.save(userBankAccount);

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transfer transfer = new Transfer();
        transfer.setOperationType(OperationType.CREDIT);
        transfer.setDescription(description);
        transfer.setTransactionDate(LocalDate.now());
        transfer.setUserBankAccount(userBankAccount);
        transfer.setAmount(amount);
        saveTransfer(transfer);
        return transfer;*/
        return null;
    }

    @Override
    public Transfer transferMoneyToUserBankAccount(int userId, int userBankId, double amount, String description) {
        if (userBankId <= 0 || userId <= 0 || amount <= 0) {
            return null;
        }
        User user = userRepository.findById(userId).orElse(null);
        UserBankAccount userBankAccount = userBankAccountRepository.findById(userBankId).orElse(null);

        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        userBankAccount.setBalance(userBankAccount.getBalance() + amount);
        userBankAccountRepository.save(userBankAccount);

        Transfer transfer = new Transfer();
        transfer.setOperationType(OperationType.DEBIT);
        transfer.setDescription(description);
        transfer.setTransactionDate(LocalDate.now());
        transfer.setUserBankAccount(userBankAccount);
        transfer.setAmount(amount);
        saveTransfer(transfer);
        return transfer;
    }
}
