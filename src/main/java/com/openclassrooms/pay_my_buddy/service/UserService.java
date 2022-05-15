package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.model.User;

import java.util.Set;

public interface UserService {

    User saveUser(User user);

    User findUserByEmail(String email);

    User login(String email, String password);

    User findUserById(int id);

    User updateUser(int id, User user);

    Set<User> getAllContactsByUser(User user);

    void addUserToContact(String userEmail, String buddyEmail);
}
