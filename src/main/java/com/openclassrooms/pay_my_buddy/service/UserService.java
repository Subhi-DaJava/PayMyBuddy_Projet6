package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.AppUser;

import java.util.Set;

public interface UserService {

    AppUser saveUser(AppUser appUser) throws EmailNotNullException;

    AppUser findUserByEmail(String email);

    AppUser login(String email, String password);

    AppUser findUserById(int id);

    AppUser updateUser(AppUser appUser) throws EmailNotNullException;

    Set<AppUser> getAllContactsByUser(int userId);

    void addUserToContact(String userEmail, String buddyEmail);
}
