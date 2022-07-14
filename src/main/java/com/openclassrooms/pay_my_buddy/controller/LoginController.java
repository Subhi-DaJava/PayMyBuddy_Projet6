package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LoginController gère Authentication, affiche un Login page personnalisé
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private SecurityService securityService;

    /**
     * Affiche un Login page personnalisé, si un user n'est pas authentifié, cette méthode retourne la page login, sinon retourne la page d'accueil (home page)
     * @return
     */
    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        return "redirect:/home";
    }

    /**
     * Endpoint ("/") retourne la page home
     * @return
     */
    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    /**
     * Afficher la page home
     * @return
     */
    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

    /**
     * Affiche le formulaire d'enregistrement d'un nouvel user
     * @param model
     * @param rePassword
     * @return
     */

    @GetMapping("/signup")
    public String signup(Model model, String rePassword){
        logger.info("signup(from LoginController) is working ...");

        AppUser appUser = new AppUser();

        model.addAttribute("appUser", appUser);
        model.addAttribute("rePassword", rePassword);

        return "signup";
    }

    /**
     * Méthode pour rajouter un nouvel user, avec ses informations, si réussi qui retourne la page addBankAccount(après retourne la page login pour s'authentifier à nouveau),
     * sinon retourne le formulaire signup
     * @param model
     * @param appUser
     * @param rePassword
     * @return
     */
    @PostMapping("/signup")
    public String addUser(Model model, AppUser appUser,
                          @RequestParam(name = "rePassword") String rePassword){

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
