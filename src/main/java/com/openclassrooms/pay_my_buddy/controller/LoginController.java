package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private SecurityService securityService;
    private UserBankAccountService userBankAccountService;

    public LoginController(SecurityService securityService, UserBankAccountService userBankAccountService) {
        this.securityService = securityService;
        this.userBankAccountService = userBankAccountService;
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        return "redirect:/home";
    }

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

    @GetMapping("/signup")
    public String signup(Model model, String rePassword){
        logger.info("signup(from LoginController) is working ...");

        AppUser appUser = new AppUser();

        model.addAttribute("appUser", appUser);
        model.addAttribute("rePassword", rePassword);

        return "signup";
    }
    @PostMapping("/signup")
    public String addUser(Model model, AppUser appUser,
                          @RequestParam(name = "rePassword") String rePassword){
      /*  String emailTaken = "This email has been taken!";
        model.addAttribute("emailTaken", emailTaken);*/

        if(securityService.loadAppUserByUserEmail(appUser.getEmail()) != null){
            String emailTaken = "This email has been taken!";
            model.addAttribute("emailTaken", emailTaken);
            return "signup";
        }
        if(appUser.getPassword().equals(rePassword)){
            securityService.saveUser(appUser);
            return "addBankAccount";
        }

        else {
            String passwordNotMatch = "Two passwords don't match each other!";
            model.addAttribute("passwordNotMatch", passwordNotMatch);
            return "signup";
        }
    }
}
