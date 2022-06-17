package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public AppUser saveUser(AppUser appUser) throws EmailNotNullException {
        logger.debug("SaveUser method starts here !!");

        AppUser appUserCheck = userRepository.findUserByEmail(appUser.getEmail());

        if ( appUser.getEmail() == null) {
            logger.debug("UserEmail should not be null");
           throw new EmailNotNullException("The field email should not be null");
        }
        if( appUserCheck != null){
            logger.debug("This user, email={}, exists already", appUser.getEmail());
            throw new UserExistingException("This user already exists in the DB.");
        }

        logger.info("This user " + appUser + " is successfully saved in the DB !!");

        appUserCheck = new AppUser();

        appUserCheck.setFirstName(appUser.getFirstName());
        appUserCheck.setLastName(appUser.getLastName());
        appUserCheck.setEmail(appUser.getEmail());
        appUserCheck.setPassword(appUser.getPassword());
        //userCheck.setBalance(0.0);
        userRepository.save(appUser);

        return appUser;

    }

    @Override
    public AppUser findUserByEmail(String email) {
        logger.debug("This method findUserByEmail starts here !!");
        AppUser appUser = userRepository.findUserByEmail(email);

        if (appUser == null) {
            logger.debug("This user doesn't exist in DB which email [" + email + "]");
            /*throw new UserNotExistingException("This user which email [" + email + "] doesn't exist yet in DB !!");*/
        }
        //déjà validator check the input : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not supported]
        if (email == null) {
            logger.debug("Email should not be null or not be empty !!");
            //throw new EmailNotNullException("Email is null or empty !!");
        }

        logger.info("This user which email [" + email + "] is successfully found !!");
        return appUser;


    }

    @Override
    public AppUser login(String email, String password) {
        AppUser appUserByEmail = findUserByEmail(email);
        if (appUserByEmail == null) {
            return null;
        } else if (!appUserByEmail.getPassword().equals(password)) {
            return null;
        } else {
            return appUserByEmail;
        }
    }

    @Override
    public AppUser findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser updateUser(AppUser appUser) throws EmailNotNullException {

        AppUser appUserUpdating = findUserByEmail(appUser.getEmail());
        if (appUserUpdating == null) {
            throw new RuntimeException("Could not update, check the User information");
        }
        appUserUpdating.setBalance(appUser.getBalance());
        appUserUpdating.setEmail(appUser.getEmail());
        appUserUpdating.setLastName(appUser.getLastName());
        appUserUpdating.setFirstName(appUser.getFirstName());
        appUserUpdating.setPassword(appUser.getPassword());

        return saveUser(appUserUpdating);
    }

    @Override
    public Set<AppUser> getAllContactsByUser(int userId) {
        AppUser appUserById = findUserById(userId);
        if (appUserById == null) {
            throw new UserNotExistingException("This id doesn't exist !!!");
        }
        Set<AppUser> appUserContacts = appUserById.getContacts();
        if (appUserContacts.isEmpty()) {
            return new HashSet<>();
        }
        return appUserContacts;
    }

    @Override
    public void addUserToContact(String userEmail, String buddyEmail) {
        logger.debug("This addUserToContacts starts here !!");
        AppUser appUserContact = userRepository.findUserByEmail(buddyEmail);
        AppUser appUser = userRepository.findUserByEmail(userEmail);

        if (appUserContact != null && appUser != null) {
            if (appUser.getContacts().contains(appUserContact)) {
                logger.debug("UserContact is already added !!");
              /*  throw new UserExistingException("This contact is added !!");*/
            }
            logger.info("This userContact which email [" + buddyEmail + "] is successfully added to this user which email [" + userEmail + "]");
            appUser.getContacts().add(appUserContact);
            return;

        }
        if (appUser != null && appUserContact == null) {
            logger.debug("UserContact doesn't exist in the DB !!");
            //throw new UserNotExistingException("This userContact which email [" + buddyEmail + "] doesn't exist yet in the DB");
        }
        if (buddyEmail == null || buddyEmail.isEmpty()) {
            logger.debug("buddyEmail should not be null or be empty neither !!");
            throw new EmailNotNullException("Not null or not empty !!");
        }
    }
}
