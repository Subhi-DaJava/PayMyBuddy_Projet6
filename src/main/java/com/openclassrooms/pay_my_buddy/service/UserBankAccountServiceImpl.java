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
    public void addBankAccountToPayMyBuddy(AppUser appUser,
                                           String bankName,
                                           String bankLocation,
                                           String codeIBAN,
                                           String codeBIC) {
        logger.debug("This addBankAccountToPayMyBuddy method (from UserBankAccountServiceImpl) starts here");
        UserBankAccount checkWithCodeIBANUserBankAccount = userBankAccountRepository.findByCodeIBAN(codeIBAN);


        if (checkWithCodeIBANUserBankAccount != null){
            logger.debug("This bankAccount with the codeIBAN={} already exists in DB", codeIBAN);
        }

        UserBankAccount userBankAccount = new UserBankAccount();
        userBankAccount.setAppUser(appUser);
        userBankAccount.setBankLocation(bankLocation);
        userBankAccount.setBankName(bankName);
        userBankAccount.setCodeBIC(codeBIC);
        userBankAccount.setCodeIBAN(codeIBAN);

        userBankAccountRepository.save(userBankAccount);
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

    //TODO: remplacer userId par userEmail, bankAccountId par codeIBAN
    @Override
    public UserBankAccount addUserToUserBankAccount(int userId, int bankAccountId) {
        logger.debug("This methode starts here");
        UserBankAccount userBankAccount = findUserBankAccountById(bankAccountId);
        AppUser appUser = userRepository.findById(userId).orElse(null);

        if (userBankAccount == null || appUser == null) {
            logger.debug("This userId [" + userId + "], or the bankAccountId [" + bankAccountId + "] doesn't exist yet !!");
            throw new UserNotExistingException("This userId [" + userId + "], or the bankAccountId [" + bankAccountId + "] doesn't exist yet !!");
        } else if (userBankAccount.getAppUser() != null && appUser.getUserBankAccount() != null) {
            logger.debug("Already associated or associated with other userId!");
            throw new UserExistingException("This userId [" + userId + "] already added to this userBankAccount which bankAccountId [" + bankAccountId + "] !!");
        } else if (userBankAccount.getAppUser() == null && appUser.getUserBankAccount() == null) {
            logger.info("This user which UserId [" + userId + "] successfully added to this userBankAccount which id [" + bankAccountId + "]");
            userBankAccount.setAppUser(appUser);
           /* addBankAccountToPayMyBuddy(userBankAccount);*/
            appUser.setUserBankAccount(userBankAccount);
            return userBankAccount;
        }
        return null;
    }

    @Override
    public void transferBetweenBankAnaPMB(String userEmail,
                                          double amount,
                                          String description,
                                          OperationType operationType) {
        logger.debug("This sendMoneyToAppUser method(from UserBankAccountServiceImpl) starts here.");

        Transfer transfer = new Transfer();

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);
        UserBankAccount userBankAccount = appUser.getUserBankAccount();

        double userBalance = appUser.getBalance();

        String BANKtoPMB = String.valueOf(OperationType.CREDIT);
        if(String.valueOf(operationType).equals(BANKtoPMB)){
            userBalance = userBalance + amount; //TODO: should pay 5% fee
        } else {
            userBalance = userBalance - amount; //TODO: should pay 5% fee
        }

        appUser.setBalance(userBalance);
        securityService.saveUser(appUser);


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

}
