package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private SecurityService securityService;

    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        return "redirect:/transfer";

    }

    @GetMapping("/")
    public String homePage(){

        return "redirect:/home";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        logger.info("signup(from LoginController) is working ...");

        AppUser appUser = new AppUser();

        model.addAttribute("appUser", appUser);
        return "signup";
    }
    @PostMapping("/signup")
    public String addUser(@ModelAttribute("appUser") AppUser appUser){

        securityService.saveUser(appUser);

        return "redirect:/login";

    }
}
