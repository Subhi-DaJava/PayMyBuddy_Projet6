package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
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
    public User updateUser(int id, User user) {

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
    public Set<User> getAllContactsByUser(User user) {

        return user.getContacts();
    }

    @Override
    public void addUserToContact(String userEmail, String buddyEmail) {
        User userExisting = findUserByEmail(buddyEmail);
        findUserByEmail(userEmail).getContacts().add(userExisting);

        if(findUserByEmail(buddyEmail) == null){
            //ask to sign up
        }

    }
}
