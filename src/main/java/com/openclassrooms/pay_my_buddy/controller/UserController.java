package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.ConnectionDTO;
import com.openclassrooms.pay_my_buddy.dto.ProfileDTO;
import com.openclassrooms.pay_my_buddy.exception.PasswordNotMatchException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * UserController : rajouter un ami à sa liste d'amis, afficher la page principale(transfer), afficher le profile d'un user, mettre à jour un user
 */
@Controller
@Transactional
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private SecurityService securityService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Rajoute un ami (existe dans la bdd) à la liste d'un utilisateur, vérifie si cet ami existe ou not à la fois dans la bdd et dans la liste d'amis,
     * si c'est réussit, retourne la page Transfer, sinon redirect addBuddy
     * @param model
     * @param buddyEmail
     * @param page
     * @return
     */
    @PostMapping("/addBuddy")
    public String addBuddy(Model model, @RequestParam(name = "buddyEmail") String buddyEmail,
                           @RequestParam(defaultValue = "0") int page) {

        logger.debug("This method addContactToUser(from UserController) starts here !!");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        if(securityService.loadAppUserByUserEmail(buddyEmail) == null){
            String userNotExist = "This the buddy not found";
            model.addAttribute("userNotExist", userNotExist);

            return "redirect:/addBuddy";
        }

       securityService.addAppUserToConnection(userEmail, buddyEmail);

        logger.info("UserEmail={} successfully added the userBuddy={}", userEmail, buddyEmail);

        return "redirect:/transfer?page=" + page;
    }

    /**
     * Renvoie le formulaire d'ajouter un ami à la liste des amis
     * @param model
     * @param buddyEmail
     * @return
     */

    @GetMapping("/addConnection")
    public String addConnection(Model model, String buddyEmail) {
        logger.debug("This GetMapping methode addConnection starts here");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String appUserEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(appUserEmail);

        String userName = appUser.getFirstName() + " " + " " + appUser.getLastName();


        model.addAttribute("buddyEmail", buddyEmail);
        model.addAttribute("userName", userName);

        return "formAddConnection";
    }

    /**
     * Afficher la page principale, dont le formulaire de transaction, la pagination pour les transactions etc
     * @param model
     * @param amount
     * @param description
     * @param buddyEmail
     * @param page
     * @param size
     * @return
     */

    @GetMapping("/transfer")
    public String showPageTransfer(Model model,
                                   String amount,
                                   String description,
                                   String buddyEmail,
                                   @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "size", defaultValue = "3", required = false) int size) {
        logger.debug("This showPageTransfer starts here");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String appUserEmail = authentication.getName();

        AppUser appUserPay = securityService.loadAppUserByUserEmail(appUserEmail);

        String name = appUserPay.getFirstName() + " " + " " + appUserPay.getLastName();

        Set<AppUser> appUserBuddyList = appUserPay.getConnections();

        Page<Transaction> listTransaction = transactionRepository.findAllBySource(PageRequest.of(page, size), appUserPay);

        model.addAttribute("totalPage", listTransaction.getTotalPages());
        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name_user", name);
        model.addAttribute("userBuddyList", appUserBuddyList);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("buddyEmail", buddyEmail);

        return "transfer";
    }

    /**
     * Renvoie le profile d'un utilisateur
     * @param model
     * @return
     */
    @GetMapping("/myProfile")
    public String myProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);
        String user_name = appUser.getFirstName() + " " + appUser.getLastName();

        ProfileDTO profile = securityService.findProfile(userEmail);

        model.addAttribute("profile", profile);
        model.addAttribute("user_name", user_name);

        return "profile";
    }

    /**
     * Revoie la liste des amis
     * @param model
     * @return
     */
    @GetMapping("/myConnections")
    public String myConnections(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        String user_name = appUser.getFirstName() + " " + appUser.getLastName();

        List<ConnectionDTO> connections = securityService.getConnections(appUser);

        model.addAttribute("connections", connections);
        model.addAttribute("user_name", user_name);

        return "myConnections";
    }

    /**
     * Affiche la page d'informations d'un user pour les mettre à jour
     * @param model
     * @return
     */
    @GetMapping("/editUser")
    public String editUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        model.addAttribute("appUser", appUser);

        return "editUser";
    }

    /**
     * Mettre à jour les informations saisies par un utilisateur, vérifier si l'email est unique ou non
     * @param firstName
     * @param lastName
     * @param email
     * @return
     */
    @PostMapping("/editUser")
    public String editProfile(String firstName, String lastName, String email){
        logger.debug("This editProfile method(from UserController) starts here.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        int userId = appUser.getAppUserid();

        if(email == null || (userRepository.findByEmail(email) != null && appUser != userRepository.findByEmail(email))){
            logger.debug("UserEmail should not be null or user already exits with this email = " + email);
            throw new RuntimeException("UserEmail should not be null or user already exits with this email = " + email);
        }
        securityService.editAppUserInfo(userId, firstName, lastName, email);

        return "login";
    }

    /**
     * Affiche le formulaire pour changer le mot de passe d'un utilisateur
     * @param model
     * @param password
     * @param rePassword
     * @return
     */
    @GetMapping("/changePassword")
    public String changePassword(Model model, String password, String rePassword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        model.addAttribute("appUser", appUser);
        model.addAttribute("rePassword", rePassword);
        model.addAttribute("password", password);

        return "updatePassword";
    }

    /**
     * Mette à jour le mot de passe, vérifier les deux mots de passe saisis sont identiques ou non
     * @param password
     * @param rePassword
     * @return
     */

    @PostMapping("/changePassword")
    public String updatePassword(@RequestParam(name = "password") String password,
                                 @RequestParam(name = "rePassword") String rePassword){

        logger.debug("This updatePassword method(from UserController) starts here.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);
        int userId = appUser.getAppUserid();

        if(!password.equals(rePassword)){
            //TODO: message error
            logger.debug("Password and rePassword should match each other(from UserController)!!");
            throw new PasswordNotMatchException("Two passwords don't match each ocher!!(from UserController)");
        }
        securityService.changePassword(userId, password, rePassword);

        logger.info("Your password is successfully changed! (from UserController)");
        return "login";
    }


}
