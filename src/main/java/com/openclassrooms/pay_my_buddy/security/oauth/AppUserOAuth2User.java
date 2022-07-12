package com.openclassrooms.pay_my_buddy.security.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
/**
 * Purpose of th class for providing an object represents the logged-in user
 */
public class AppUserOAuth2User implements OAuth2User {
    private static final Logger logger = LoggerFactory.getLogger(AppUserOAuth2User.class);
    private OAuth2User oAuth2User;

    public AppUserOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        logger.info("CustomOAth2User is working for getAttributes method");
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //TODO: If an Admin sign in?
        Collection<GrantedAuthority> authorities = new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("USER")));
        logger.info("CustomOAth2User is working for getAuthorities method and authorities: " + authorities);
        return authorities;
    }

    @Override
    public String getName() {
        logger.info("CustomOAth2User is working for getName method and email=" + oAuth2User.getAttribute("email"));
        return oAuth2User.getAttribute("email"); // the email of Google account
    }

    public String getEmail(){
        logger.info("CustomOAth2User is working for getEmail method");
        return oAuth2User.getAttribute("name"); // get the name of Google account
    }
}
