package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;

public interface SecurityService {

    AppUser saveNewUser(String firstName, String lastName, String email,
                        String password, String rePassword, double balance);

    Role saveNewRole(String roleName);

    AppUser loadAppUserByUserEmail(String email);

    void deleteAppUserById(Integer appUserId);

    Role loadRoleByRoleName(String roleName);

}
