package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.dto.BankAccountDTO;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transfer;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.repository.UserBankAccountRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.hibernate.annotations.DynamicUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

@Service
@Transactional
@DynamicUpdate
public class UserBankAccountServiceImpl implements UserBankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(UserBankAccountServiceImpl.class);
    @Autowired
    private UserBankAccountRepository userBankAccountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityService securityService;

    @Autowired
    private TransferService transferService;

    @Override
    public void addBankAccountToPayMyBuddy(String userEmail,
                                           String bankName,
                                           String bankLocation,
                                           String codeIBAN,
                                           String codeBIC) {
        logger.debug("This addBankAccountToPayMyBuddy method (from UserBankAccountServiceImpl) starts here");
        UserBankAccount userBankAccount = userBankAccountRepository.findByCodeIBAN(codeIBAN);
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        if(appUser == null || userEmail == null ){
            logger.debug("This User with UserEmail doesn't exist in DB or userEmail should not be null!! (from UserBankAccountServiceImpl)");
            throw new RuntimeException("user with email="+userEmail+"doesn't exit or userEmail should not be null");
        }

        if (userBankAccount != null){
            logger.debug("This bankAccount with the codeIBAN={} already exists in DB !!(from UserBankAccountServiceImpl)", codeIBAN);
            throw new RuntimeException("This bank account with codeIABN=" + codeIBAN + " already is registered !!(from UserBankAccountServiceImpl)");
        }
        if(appUser.getUserBankAccount() == null){
            userBankAccount = new UserBankAccount();
            userBankAccount.setAppUser(appUser);
            userBankAccount.setBankLocation(bankLocation);
            userBankAccount.setBankName(bankName);
            userBankAccount.setCodeBIC(codeBIC);
            userBankAccount.setCodeIBAN(codeIBAN);

            UserBankAccount newUserBankAccountAddUser = userBankAccountRepository.save(userBankAccount);
            appUser.setUserBankAccount(newUserBankAccountAddUser);
            userRepository.save(appUser);
        } else {
            logger.debug("This user with email={} has already a bank account !!(from UserBankAccountServiceImpl) ", userEmail);
            throw new RuntimeException("This user with UserEmail=" + userEmail + " already has an userAccount !! (UserBankAccountServiceImpl)");
        }

    }

    @Override
    public UserBankAccount findUserBankAccountById(int id) {
        UserBankAccount userBankAccount = userBankAccountRepository.findById(id).orElse(null);
        if (userBankAccount == null) {
            logger.debug("This id [" + id + "] doesn't exist");
            throw new UserNotExistingException("This userBankAccount which id [" + id + "] doesn't exist yer !!");
        }
        return userBankAccount;
    }

    @Override
    public void transferBetweenBankAndPMB(String userEmail,
                                          double amount,
                                          String description,
                                          OperationType operationType) {
        logger.debug("This sendMoneyToAppUser method(from UserBankAccountServiceImpl) starts here.");

        Transfer transfer = new Transfer();

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);
        UserBankAccount userBankAccount = appUser.getUserBankAccount();

        double userBalance = appUser.getBalance();

        if (userBankAccount == null){
            throw new RuntimeException("This user non bank account associated !! please add a bank account!");
        }

        String BANKtoPMB = String.valueOf(OperationType.CREDIT);
        if(String.valueOf(operationType).equals(BANKtoPMB)){
            userBalance = userBalance + amount; //TODO: should pay 5% fee
        } else {
            if(userBalance > amount)
                userBalance = userBalance - amount; //TODO: should pay 5% fee
            else
                throw new RuntimeException("Balance not enough !!(from UserBankAccountServiceImpl)");
        }
        appUser.setBalance(userBalance);

        transfer.setAmount(amount);
        transfer.setDescription(description);
        transfer.setOperationType(operationType);
        transfer.setTransactionDate(LocalDate.now());
        transfer.setUserBankAccount(userBankAccount);
        transferService.saveTransfer(transfer);
    }

    @Override
    public BankAccountDTO bankAccountInfo(AppUser appUser) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();

        UserBankAccount userBankAccount = userBankAccountRepository.findByAppUser(appUser);

        bankAccountDTO.setBankName(userBankAccount.getBankName());
        bankAccountDTO.setBankLocation(userBankAccount.getBankLocation());
        bankAccountDTO.setCodeBIC(userBankAccount.getCodeBIC());
        bankAccountDTO.setCodeIBAN(userBankAccount.getCodeIBAN());

        return bankAccountDTO;
    }

    @Override
    public UserBankAccount findByCodeIBAN(String codeIBAN) {
        logger.debug("This findByCodeIBAN method (from UserBankAccountServiceImpl) starts here.");
       UserBankAccount findUserBankAccountByCodeIBAN = userBankAccountRepository.findByCodeIBAN(codeIBAN);
       if(findUserBankAccountByCodeIBAN == null){
           logger.debug("This bank account not found with this codeIBAN={} not found in DB (UserBankAccountServiceImpl)", codeIBAN);
           return null;
       }
       if(codeIBAN == null || codeIBAN.isEmpty()){
           logger.debug("CodeIBAN should not be null or empty (form UserBankAccountServiceImpl).");
           return null;
       }
        return findUserBankAccountByCodeIBAN;
    }

}
