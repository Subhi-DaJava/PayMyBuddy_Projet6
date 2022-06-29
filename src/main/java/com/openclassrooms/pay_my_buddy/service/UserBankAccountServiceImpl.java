package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
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

import javax.transaction.Transactional;
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
    public UserBankAccount saveUserBankAccount(UserBankAccount userBankAccount) {
        if (userBankAccount == null) {
            return null; // à améliorer
        }
        UserBankAccount newUserBankAccount = new UserBankAccount();
        newUserBankAccount.setBankName(userBankAccount.getBankName());
        newUserBankAccount.setBankLocation(userBankAccount.getBankLocation());
        newUserBankAccount.setCodeBIC(userBankAccount.getCodeBIC());
        newUserBankAccount.setCodeIBAN(userBankAccount.getCodeIBAN());
        newUserBankAccount.setBalance(userBankAccount.getBalance());

        return userBankAccountRepository.save(userBankAccount);
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
    public AppUser findUserByUserBankAccountId(int id) {
        UserBankAccount userBankAccount = findUserBankAccountById(id);
        AppUser appUser = userBankAccount.getAppUser();
        return appUser;
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
            saveUserBankAccount(userBankAccount);
            appUser.setUserBankAccount(userBankAccount);
            return userBankAccount;
        }
        return null;
    }

    @Override
    public void sendMoneyToAppUser(String codeIBAN,
                                   String userEmail,
                                   double amount,
                                   String description,
                                   OperationType operationType) {
        logger.debug("This sendMoneyToAppUser method(from UserBankAccountServiceImpl) starts here.");
        Transfer transfer = new Transfer();

        UserBankAccount userBankAccount = userBankAccountRepository.findByCodeIBAN(codeIBAN);
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        if(userBankAccount == null){
            logger.debug("This userBankAccount not found!!(from sendMoneyToAppUser)");
            throw new RuntimeException("This UserBankAccount with the codeIBAN=" + codeIBAN + " doesn't exist yet in DB!"); //TODO: UserBankAccountNotFoundException
        }
        if(appUser == null){
            logger.debug("This appUser not found!!(from sendMoneyToAppUser)");
            throw new UserNotExistingException("This appUser with the userEmail=" + userEmail + " doesn't exist yet in DB!");
        }

        double bankBalance = userBankAccount.getBalance();
        double userBalance = appUser.getBalance();

        if(bankBalance <= amount){
            logger.debug("UserBankAccount's balance should greater than amont !(from sendMoneyToAppUser)");
            throw new RuntimeException("No enough money in the user bank account! (from sendMoneyToAppUser)");
        }

        userBalance = userBalance + amount;
        appUser.setBalance(userBalance);
        securityService.saveUser(appUser);

        bankBalance = bankBalance - amount;
        userBankAccount.setBalance(bankBalance);
        userBankAccountRepository.save(userBankAccount);

        transfer.setAmount(amount);
        transfer.setUserBankAccount(userBankAccount);
        transfer.setDescription(description);
        transfer.setOperationType(operationType);
        transfer.setTransactionDate(LocalDate.now());

        transferService.saveTransfer(transfer);

    }
}
