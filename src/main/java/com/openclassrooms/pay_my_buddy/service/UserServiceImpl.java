package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public User saveUser(User user) throws EmailNotNullException {
        User userCheck = findUserByEmail(user.getEmail());

        if(userCheck == null){
            userRepository.save(user);
            return user;
        }
        else if(userCheck != null)
            throw new UserExistingException("This user already exist in the DB.");

        else if(user.getEmail().isEmpty()){
            throw new EmailNotNullException("This field should not be null !!! ");
        }
        else
            return null;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            return null;
        return user;
    }

    @Override
    public User login(String email, String password) {
        User userByEmail = findUserByEmail(email);
        if(userByEmail == null){
            return null;
        }
        else if(!userByEmail.getPassword().equals(password)){
            return null;
        }
        else
            return userByEmail;
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(int id, User user) throws EmailNotNullException {

        User userUpdating = findUserById(id);
        if(userUpdating == null)
            throw new RuntimeException("Could not update, check the User information");
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
        if(userById == null){
            throw new UserNotExistingException("This id doesn't exist !!! ");
        }
        Set<User> userContacts = userById.getContacts();
        if (userContacts.isEmpty())
            return new HashSet<>();
        return userContacts;
    }

    @Override
    public Boolean addUserToContact(String userEmail, String buddyEmail) {

        User userExisting = findUserByEmail(buddyEmail);
        if(userExisting != null){
            return findUserByEmail(userEmail).getContacts().add(userExisting);
        }

        if(findUserByEmail(buddyEmail) == null){
           throw new UserNotExistingException("This buddy doesn't added to DB !");
        }
        return false;
    }
}
