package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.dto.ProfileDTO;
import com.openclassrooms.pay_my_buddy.exception.*;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.repository.RoleRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public AppUser saveUser(AppUser appUser) {
        logger.debug("SaveUser method starts here !!");

        AppUser appUserCheck = userRepository.findByEmail(appUser.getEmail());

        if ( appUser.getEmail() == null) {
            logger.debug("UserEmail should not be null!!");
            throw new EmailNotNullException("The field email should not be null");
        }
        if( appUserCheck != null){
            logger.info("This user, email={}, exists already, and update this appUser's information(from UsrServiceImpl)", appUser.getEmail());
            appUserCheck.setFirstName(appUser.getFirstName());
            appUserCheck.setLastName(appUser.getLastName());
            appUserCheck.setEmail(appUser.getEmail());
            appUserCheck.setPassword(passwordEncoder().encode(appUser.getPassword()));
            appUserCheck.setBalance(appUser.getBalance());
            return appUserCheck;
        }
        appUserCheck = new AppUser();

        appUserCheck.setFirstName(appUser.getFirstName());
        appUserCheck.setLastName(appUser.getLastName());
        appUserCheck.setEmail(appUser.getEmail());
        appUserCheck.setPassword(passwordEncoder().encode(appUser.getPassword()));
        appUserCheck.setBalance(0.0);

        AppUser appUserSaved = userRepository.save(appUserCheck);
        logger.info("This user " + appUser + " is successfully saved in the DB !!(from UserServiceImpl)");

        return appUserSaved;
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
    public void addRoleToUse(String userEmail, String roleName) {

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

    @Override
    public ProfileDTO findProfile(String email) {
        logger.debug("This method findProfile(from UserServiceImpl) starts here.");
        ProfileDTO profileDTO = new ProfileDTO();

        AppUser appUser = userRepository.findByEmail(email);

        if(appUser == null){
            logger.debug("This profile doesn't existe with this email=" +email);
            throw new UserNotExistingException("This profile doesn't not exist with this email=" + email);
        }
        String userFirstName = appUser.getFirstName();
        String userLastName = appUser.getLastName();
        String userEmail = appUser.getEmail();
        double balance = appUser.getBalance();
        List<Role> roles = appUser.getRoles();
        Set<String> userRoles = new HashSet<>();
        for (Role role : roles) {
            userRoles.add(role.getRoleName());
        }

        Set<AppUser> contacts = appUser.getContacts();
        Set<String> contactEmails = new HashSet<>();
        for (AppUser buddy : contacts) {
            contactEmails.add(buddy.getFirstName() + " " + buddy.getLastName());
        }
        profileDTO.setFirstName(userFirstName);
        profileDTO.setLastName(userLastName);
        profileDTO.setEmail(userEmail);
        profileDTO.setRoles(userRoles);
        profileDTO.setContacts(contactEmails);
        profileDTO.setBalance(balance);

        return profileDTO;
    }

    @Override
    public void addAppUserToContact(String userEmail, String buddyEmail) {
        logger.debug("This addUserToContacts starts here !!");
        AppUser appUserContact = userRepository.findByEmail(buddyEmail);
        AppUser appUser = userRepository.findByEmail(userEmail);

        if (appUserContact != null && appUser != null) {
            if (appUser.getContacts().contains(appUserContact)) {
                logger.debug("UserContact is already added !!");
                /*  throw new UserExistingException("This contact is added !!");*/
            }
            logger.info("This userContact which email [" + buddyEmail + "] is successfully added to this user which email [" + userEmail + "]");
            appUser.getContacts().add(appUserContact);
            return;

        }
        if (appUser != null && appUserContact == null) {
            logger.debug("UserContact doesn't exist in the DB !!");
            //throw new UserNotExistingException("This userContact which email [" + buddyEmail + "] doesn't exist yet in the DB");
        }
        if (buddyEmail == null || buddyEmail.isEmpty()) {
            logger.debug("BuddyEmail should not be null or be empty neither !!");
            throw new EmailNotNullException("Not null or not empty !!");
        }
    }

    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //TODO: une functionality pour afficher tous les emails( nom et prénom peut être) des contact d'une AppUser
}
