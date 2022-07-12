package com.openclassrooms.pay_my_buddy.security.oauth;

import com.openclassrooms.pay_my_buddy.constant.AuthenticationProvider;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Autowired
    private SecurityService securityService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.debug("This onAuthenticationSuccess method(from OAuth2LoginSuccessHandler) starts here.");

        AppUserOAuth2User oAuth2User = (AppUserOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getName();
        String name = oAuth2User.getEmail();

        //Check appUser's with this email exists or not
        AppUser appUser = securityService.loadAppUserByUserEmail(email);

        if( appUser == null ){
            // register as a new AppUser
            securityService.createNewAppUserAfterOAuthLoginSuccess(email, name, AuthenticationProvider.GOOGLE);
        } else {
            // update existing AppUser
            securityService.updateAppUserAfterOAuthLoginSuccess(appUser, name, AuthenticationProvider.GOOGLE);

        }

        logger.info("Google account email={}", email);
        logger.info("Google account name={}", name);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
