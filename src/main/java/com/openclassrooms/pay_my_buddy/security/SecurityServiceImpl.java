package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.exception.PasswordNotMatchException;
import com.openclassrooms.pay_my_buddy.exception.RoleExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.repository.RoleRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public SecurityServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public AppUser saveNewUser(String firstName, String lastName, String userEmail, String password, String rePassword, double balance) {
        logger.debug("This method saveNewUser(SecurityServiceImpl) starts here");

        AppUser appUser = userRepository.findByEmail(userEmail);

        if(appUser != null){
            logger.debug("This appUser with email={} is exist already in DB", userEmail);
            throw new UserExistingException("This email= " + userEmail + " exists in DB");
        }
        if (!password.equals(rePassword)){
            logger.debug("rePassword doesn't match with password");
            throw new PasswordNotMatchException("Password not match");
        }
        logger.info("PasswordEncoder is hashing the password...");
        String hashedPassword = passwordEncoder().encode(password);

        appUser = new AppUser();
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setEmail(userEmail);
        appUser.setPassword(hashedPassword);
        appUser.setBalance(0.0);

        AppUser savedAppUser = userRepository.save(appUser);
        logger.info("This newUser with email={} is successfully saved in DB", userEmail);
        return savedAppUser;
    }

    @Override
    public Role saveNewRole(String roleName) {
        logger.debug("This method saveNewRole(ServiceSecurityImpl) starts here");
        Role role = roleRepository.findByRoleName(roleName);

        if(role != null){
            logger.debug("This role={} is already in DB", roleName);
            throw new RoleExistingException("This role= " + roleName + " exists in DB");
        }
        role = new Role();

        role.setRoleName(roleName);

        Role savedRole = roleRepository.save(role);
        logger.info("This newRole with roleName={} is successfully saved in DB", roleName);
        return savedRole;
    }

    @Override
    public AppUser loadAppUserByUserEmail(String userEmail) {
        logger.debug("This methode loadAppUserByUserEmail(SecurityServiceImpl) starts here");

        AppUser appUser = userRepository.findByEmail(userEmail);

        if(appUser == null){
            logger.debug("This appUser with userEmail={} doesn't exist in DB(From SecurityServiceImpl)", userEmail);
            throw new UserNotExistingException("This appUser with email= " + userEmail + " not found in DB(From SecurityServiceImpl)");
        }
        logger.info("This appUser which email={} is successfully loaded", userEmail);
        return appUser;
    }

    @Override
    public void deleteAppUserById(Integer appUserId) {

        userRepository.deleteById(appUserId);
    }

    @Override
    public Role loadRoleByRoleName(String roleName) {
        logger.debug("This method loadRoleByRoleName(SecurityServiceImpl) starts here");

        Role role = roleRepository.findByRoleName(roleName);

        if(role == null){
            logger.debug("This role={} doesn't exist in DB", roleName);
        }

        return role;
    }

    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //TODO: une functionality pour afficher tous les emails( nom et prénom peut être) des contact d'une AppUser
}
