package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private SecurityService securityService;

    public UserDetailsServiceImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        logger.debug("This loadUserByUserEmail(UserDetailsServiceImpl) starts here");

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        if(appUser == null){
            logger.debug("This appUser which email={} doesn't exist in DB", userEmail);
            throw new UsernameNotFoundException("This appUser doesn't exist");
        }

        Collection<GrantedAuthority> authorities =
                appUser.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .collect(Collectors.toList());

        if(authorities.isEmpty()){
            logger.info("This appUser has any role yet !!");
        }

        User user = new User(appUser.getEmail(), appUser.getPassword(), authorities);
        logger.info("This user with email={} is successfully authenticated!", userEmail);

        return user;
    }

/*    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }*/
}
