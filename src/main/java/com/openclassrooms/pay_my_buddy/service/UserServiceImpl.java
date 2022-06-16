package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.User;
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
    public User saveUser(User user) throws EmailNotNullException {
        logger.debug("SaveUser method starts here !!");

        User userCheck = userRepository.findUserByEmail(user.getEmail());

        if ( user.getEmail() == null) {
            logger.debug("UserEmail should not be null");
           throw new EmailNotNullException("The field email should not be null");
        }
        if( userCheck != null){
            logger.debug("This user, email={}, exists already", user.getEmail());
            throw new UserExistingException("This user already exists in the DB.");
        }

        logger.info("This user " + user + " is successfully saved in the DB !!");

        userCheck = new User();

        userCheck.setFirstName(user.getFirstName());
        userCheck.setLastName(user.getLastName());
        userCheck.setEmail(user.getEmail());
        userCheck.setPassword(user.getPassword());
        //userCheck.setBalance(0.0);
        userRepository.save(user);

        return user;

    }

    @Override
    public User findUserByEmail(String email) {
        logger.debug("This method findUserByEmail starts here !!");
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            logger.debug("This user doesn't exist in DB which email [" + email + "]");
            /*throw new UserNotExistingException("This user which email [" + email + "] doesn't exist yet in DB !!");*/
        }
        //déjà validator check the input : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not supported]
        if (email == null) {
            logger.debug("Email should not be null or not be empty !!");
            //throw new EmailNotNullException("Email is null or empty !!");
        }

        logger.info("This user which email [" + email + "] is successfully found !!");
        return user;


    }

    @Override
    public User login(String email, String password) {
        User userByEmail = findUserByEmail(email);
        if (userByEmail == null) {
            return null;
        } else if (!userByEmail.getPassword().equals(password)) {
            return null;
        } else {
            return userByEmail;
        }
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(User user) throws EmailNotNullException {

        User userUpdating = findUserByEmail(user.getEmail());
        if (userUpdating == null) {
            throw new RuntimeException("Could not update, check the User information");
        }
        userUpdating.setBalance(user.getBalance());
        userUpdating.setEmail(user.getEmail());
        userUpdating.setLastName(user.getLastName());
        userUpdating.setFirstName(user.getFirstName());
        userUpdating.setPassword(user.getPassword());

        return saveUser(userUpdating);
    }

    @Override
    public Set<User> getAllContactsByUser(int userId) {
        User userById = findUserById(userId);
        if (userById == null) {
            throw new UserNotExistingException("This id doesn't exist !!!");
        }
        Set<User> userContacts = userById.getContacts();
        if (userContacts.isEmpty()) {
            return new HashSet<>();
        }
        return userContacts;
    }

    @Override
    public void addUserToContact(String userEmail, String buddyEmail) {
        logger.debug("This addUserToContacts starts here !!");
        User userContact = userRepository.findUserByEmail(buddyEmail);
        User user = userRepository.findUserByEmail(userEmail);

        if (userContact != null && user != null) {
            if (user.getContacts().contains(userContact)) {
                logger.debug("UserContact is already added !!");
              /*  throw new UserExistingException("This contact is added !!");*/
            }
            logger.info("This userContact which email [" + buddyEmail + "] is successfully added to this user which email [" + userEmail + "]");
            user.getContacts().add(userContact);
            return;

        }
        if (user != null && userContact == null) {
            logger.debug("UserContact doesn't exist in the DB !!");
            //throw new UserNotExistingException("This userContact which email [" + buddyEmail + "] doesn't exist yet in the DB");
        }
        if (buddyEmail == null || buddyEmail.isEmpty()) {
            logger.debug("buddyEmail should not be null or be empty neither !!");
            throw new EmailNotNullException("Not null or not empty !!");
        }
    }
}
