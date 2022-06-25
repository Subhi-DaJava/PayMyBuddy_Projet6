package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.AppUser;

import java.util.Set;

public interface UserService {

    AppUser saveUser(AppUser appUser) throws EmailNotNullException;

    AppUser findAppUserByEmail(String email);

    AppUser login(String email, String password);

    AppUser findAppUserById(int id);

    AppUser updateAppUser(AppUser appUser) throws EmailNotNullException;

    Set<AppUser> getAllContactsByAppUser(int userId);

    void addAppUserToContact(String userEmail, String buddyEmail);

    
}
