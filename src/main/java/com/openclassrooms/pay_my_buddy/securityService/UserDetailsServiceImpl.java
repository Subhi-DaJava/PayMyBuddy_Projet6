package com.openclassrooms.pay_my_buddy.securityService;

import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        logger.debug("This loadUserByUsername(in UserDetailsServiceImpl) method stars here");

        User userPayMyBuddy = userService.findUserByEmail(userEmail);
        if(userPayMyBuddy == null){
            throw new UsernameNotFoundException("This user doesn't exist");
        }



    /*    User userPayMyBuddy = userService.findUserByEmail(userEmail);

        if (userPayMyBuddy == null){
            logger.debug("This userEmail={} doesn't exit in DB", userEmail);
            throw new UsernameNotFoundException("This userEmail= "+ userEmail + "doesn't exit in DB");
        }
        *//**
         * programmation déclarative, map() -> pour chaque appUser
         * pour chaque role on créé un objet de type SimpleGrantedAuthority, puis le rajouter dans la collection authorities
         *//*
        Collection<GrantedAuthority> authorities =
                userPayMyBuddy.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
        .collect(Collectors.toList());

        if(authorities.isEmpty()){
            logger.info("For the moment this user: "+ userEmail + " has any role !!");
        }
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(userPayMyBuddy.getEmail(), userPayMyBuddy.getPassword(), authorities);*/

        return new MyBuddyPayUserDetails(userPayMyBuddy);
    }
}
