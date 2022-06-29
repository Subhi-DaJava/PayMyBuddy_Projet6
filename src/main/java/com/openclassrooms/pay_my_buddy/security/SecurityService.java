package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.constant.AuthenticationProvider;
import com.openclassrooms.pay_my_buddy.dto.ProfileDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;

public interface SecurityService {

    AppUser saveUser(AppUser appUser);

    Role saveNewRole(String roleName);

    AppUser loadAppUserByUserEmail(String email);

    void deleteAppUserById(Integer appUserId);

    void addRoleToUser(String userEmail, String roleName);

    Role loadRoleByRoleName(String roleName);

    ProfileDTO findProfile(String email);

    void addAppUserToContact(String userEmail, String buddyEmail);

    void createNewAppUserAfterOAuthLoginSuccess(String email,
                                                String name,
                                                AuthenticationProvider provider);

    void updateAppUserAfterOAuthLoginSuccess(AppUser appUser, String name, AuthenticationProvider google);
}
