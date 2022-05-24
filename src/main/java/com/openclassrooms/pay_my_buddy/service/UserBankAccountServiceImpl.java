package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.repository.UserBankAccountRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.hibernate.annotations.DynamicUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@DynamicUpdate
public class UserBankAccountServiceImpl implements UserBankAccountService {

    private  static  final Logger logger = LoggerFactory.getLogger(UserBankAccountServiceImpl.class);
    @Autowired
    private UserBankAccountRepository userBankAccountRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserBankAccount saveUserBankAccount(UserBankAccount userBankAccount) {
        if(userBankAccount == null){
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
        if(userBankAccount == null){
            logger.debug("This id ["+id+"] doesn't exist");
            throw new UserNotExistingException("This userBankAccount which id ["+id+"] doesn't exist yer !!");
        }
        return userBankAccount;
    }

    @Override
    public User findUserByUserBankAccountId(int id) {
        UserBankAccount userBankAccount = findUserBankAccountById(id);
        User user = userBankAccount.getUser();
        return user;
    }

    @Override
    public UserBankAccount addUserToUserBankAccount(int userId, int bankAccountId){
        logger.debug("This methode starts here");
        UserBankAccount userBankAccount = findUserBankAccountById(bankAccountId);
        User user = userRepository.findById(userId).orElse(null);

        if(userBankAccount == null || user == null){
            logger.debug("This userId ["+userId+"], or the bankAccountId ["+bankAccountId+"] doesn't exist yet !!");
            throw new UserNotExistingException("This userId ["+userId+"], or the bankAccountId ["+bankAccountId+"] doesn't exist yet !!");
        }

        else if(userBankAccount.getUser() != null && user.getUserBankAccount() != null){
            logger.debug("Already associated or associated with other userId!");
            throw new UserExistingException("This userId ["+userId+"] already added to this userBankAccount which bankAccountId ["+bankAccountId+"] !!");
        }
        else if(userBankAccount.getUser() == null && user.getUserBankAccount() == null){
            logger.info("This user which UserId ["+userId+"] successfully added to this userBankAccount which id ["+bankAccountId+"]");
            userBankAccount.setUser(user);
            saveUserBankAccount(userBankAccount);
            user.setUserBankAccount(userBankAccount);
            return userBankAccount;
        }
        return null;
    }
}
