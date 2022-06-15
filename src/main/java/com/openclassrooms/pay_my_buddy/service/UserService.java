package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.User;

import java.util.Set;

public interface UserService {

    User saveUser(User user) throws EmailNotNullException;

    User findUserByEmail(String email);

    User login(String email, String password);

    User findUserById(int id);

    User updateUser(User user) throws EmailNotNullException;

    Set<User> getAllContactsByUser(int userId);

    void addUserToContact(String userEmail, String buddyEmail);
}
