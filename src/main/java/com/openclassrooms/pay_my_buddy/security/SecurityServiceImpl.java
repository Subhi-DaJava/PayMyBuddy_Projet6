package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.repository.RoleRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public AppUser saveNewUser(String firstName, String lastName, String email, String password, String rePassword, double balance) {

        return null;
    }

    @Override
    public Role saveNewRole(String roleName) {
        return null;
    }

    @Override
    public AppUser loadAppUserByUserEmail(String email) {
        return null;
    }

    @Override
    public void deleteAppUserById(Integer appUserId) {

    }

    @Override
    public Role loadRoleByRoleName(String roleName) {
        return null;
    }


    //TODO: une functionality pour afficher tous les emails( nom et prénom peut être) des contact d'une AppUser
}
