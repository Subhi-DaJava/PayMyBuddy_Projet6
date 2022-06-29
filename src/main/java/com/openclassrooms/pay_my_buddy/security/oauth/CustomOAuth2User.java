package com.openclassrooms.pay_my_buddy.security.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Purpose of th class for providing an object represents the logged-in user
 */
public class CustomOAuth2User implements OAuth2User {
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2User.class);
    private OAuth2User oAuth2User;

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        logger.info("CustomOAth2User is working for getAttributes method");
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.info("CustomOAth2User is working for getAuthorities method");
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        logger.info("CustomOAth2User is working for getName method");
        return oAuth2User.getAttribute("name"); // the name of Google account
    }

    public String getEmail(){
        logger.info("CustomOAth2User is working for getEmail method");
        return oAuth2User.getAttribute("email"); // get the email of Google account
    }

}
