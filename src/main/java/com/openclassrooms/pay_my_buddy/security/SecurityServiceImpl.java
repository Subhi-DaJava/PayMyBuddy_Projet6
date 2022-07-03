package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.constant.AuthenticationProvider;
import com.openclassrooms.pay_my_buddy.dto.ConnectionDTO;
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

import java.util.ArrayList;
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
            appUserCheck.setBalance(appUser.getBalance());
            return appUserCheck;
        }
        appUserCheck = new AppUser();

        //Add role=USER by default to a new AppUser
        Role addRoleUSER = loadRoleByRoleName("USER");

        appUserCheck.setFirstName(appUser.getFirstName());
        appUserCheck.setLastName(appUser.getLastName());
        appUserCheck.setEmail(appUser.getEmail());
        appUserCheck.setPassword(passwordEncoder().encode(appUser.getPassword()));
        appUserCheck.setBalance(0.0);
        appUserCheck.getRoles().add(addRoleUSER);

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

        logger.info("This appUser which email={} is successfully loaded", userEmail);
        return appUser;
    }

    @Override
    public void deleteAppUserById(Integer appUserId) {

        userRepository.deleteById(appUserId);
    }

    @Override
    public void addRoleToUser(String userEmail, String roleName) {
        logger.debug("This addRoleToUser(from SecurityServiceImpl) starts here");
        AppUser appUser = loadAppUserByUserEmail(userEmail);
        if(appUser == null){
            logger.debug("No this user with the email" + userEmail + " in the DB !");
            throw new UserNotExistingException("This user with email= " + userEmail + " doesn't exist in DB");
        }
        Role role = loadRoleByRoleName(roleName);
        if(role == null){
            logger.debug("No this role: " + roleName + " in the DB! (from addRoleToUser method)");
            throw new RoleExistingException("This role: " + roleName + " not found in DB");
        }

        appUser.getRoles().add(role);
        logger.info("This role= " + roleName + " is added to this appUser which email={}", userEmail);
    }

    @Override
    public Role loadRoleByRoleName(String roleName) {
        logger.debug("This method loadRoleByRoleName(SecurityServiceImpl) starts here");

        Role role = roleRepository.findByRoleName(roleName);

        if(role == null){
            logger.debug("This role={} doesn't exist in DB", roleName);
            throw new RoleExistingException("This role: " + roleName + " not found !");
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

        Set<AppUser> contacts = appUser.getConnections();
        Set<String> contactEmails = new HashSet<>();
        for (AppUser buddy : contacts) {
            contactEmails.add(buddy.getFirstName() + " " + buddy.getLastName());
        }
        profileDTO.setFirstName(userFirstName);
        profileDTO.setLastName(userLastName);
        profileDTO.setEmail(userEmail);
        profileDTO.setRoles(userRoles);
        profileDTO.setConnections(contactEmails);
        profileDTO.setBalance(balance);

        return profileDTO;
    }

    @Override
    public void addAppUserToContact(String userEmail, String buddyEmail) {
        logger.debug("This addUserToContacts starts here !!");
        AppUser appUserContact = userRepository.findByEmail(buddyEmail);
        AppUser appUser = userRepository.findByEmail(userEmail);

        if (appUserContact != null && appUser != null) {
            if (appUser.getConnections().contains(appUserContact)) {
                logger.debug("UserContact is already added !!");
                /*  throw new UserExistingException("This contact is added !!");*/
            }
            logger.info("This userContact which email [" + buddyEmail + "] is successfully added to this user which email [" + userEmail + "]");
            appUser.getConnections().add(appUserContact);
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

    @Override
    public void createNewAppUserAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider provider) {
        logger.info("This createNewAppUserAfterOAuthLoginSuccess(from AppUserOAuth2UserService) method" +
                "is creating the new AppUser after the first authentication with Google account.");

        AppUser appUser = new AppUser();

        appUser.setFirstName(name);
        appUser.setLastName("EMPTY_LastName");
        appUser.setEmail(email);
        appUser.setAuthProvider(provider);
       userRepository.save(appUser);
    }

    @Override
    public void updateAppUserAfterOAuthLoginSuccess(AppUser appUser, String name, AuthenticationProvider provider) {
        logger.info("This updateAppUserAfterOAuthLoginSuccess(from AppUserOAuth2UserService) method"
        + " is working for updating the AppUser with Google account authentication.");

        appUser.setFirstName(name);
        appUser.setLastName("EMPTY_LastName");
        appUser.setAuthProvider(provider);

        userRepository.save(appUser);
    }

    @Override
    public List<ConnectionDTO> getConnections(AppUser appUser) {
        logger.debug("This getConnections method(from SecurityServiceImpl) starts here.");

        List<ConnectionDTO> connectionsDTO = new ArrayList<>();
        ConnectionDTO connectionDTO;

        Set<AppUser> connections = appUser.getConnections();

        for (AppUser connection : connections){
            connectionDTO = new ConnectionDTO();

            connectionDTO.setFirstName(connection.getFirstName());
            connectionDTO.setLastName(connection.getLastName());
            connectionDTO.setEmail(connection.getEmail());

            connectionsDTO.add(connectionDTO);
        }

        return connectionsDTO;
    }

    @Override
    public void editAppUserInfo(int userId,
                                String firstName,
                                String lastName,
                                String email) {
        logger.debug("This editAppUserInfo methode (from SecurityServiceImpl) starts here.");
        AppUser appUser = userRepository.findById(userId).orElse(null);

        if(email == null || loadAppUserByUserEmail(email) != null){
            logger.debug("Email should not be null or this Email = " + email + " already exits in DB!");
            throw new RuntimeException("Email should not be null or this Email = " + email + " already exits in DB!");
        }
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setEmail(email);

        userRepository.save(appUser);
        logger.info("User's profile successfully edited!");
    }

    @Override
    public void changePassword(int userId, String password, String rePassword) {
        logger.debug("This changePassword methode(from SecurityServiceImpl) starts here.");
        AppUser appUser = userRepository.findById(userId).orElse(null);

        if(password.equals(rePassword)){
            appUser.setPassword(passwordEncoder().encode(password));
            userRepository.save(appUser);
        }else {
            logger.debug("Two passwords entered dont match each other !");
            throw new PasswordNotMatchException("Password doesn't match!");
        }
        logger.info("Password successfully changed!");
    }

    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
