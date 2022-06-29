package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.ProfileDTO;
import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
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

@Controller
@Transactional
/*@RequestMapping("/pay-my-buddy")*/
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/addBuddy")
    public String addBuddy(Model model,
                           @ModelAttribute("userEmail") String userEmail,
                           @RequestParam(name = "contactEmail") String contactEmail,
                           @RequestParam(defaultValue = "0") int page) {

        logger.debug("This method addContactToUser(from UserController) starts here !!");

        AppUser appUserBuddy =securityService.loadAppUserByUserEmail(contactEmail);

        if(contactEmail == null || appUserBuddy == null){
            logger.debug("UserBuddyEmail={} should not be null or userBuddy doesn't exist in DB which the email.(from UserController)", contactEmail);
            return "error/Bad_Operation";
        }


       securityService.addAppUserToContact(userEmail, contactEmail);

        logger.info("UserEmail={} successfully added the userBuddy={}", userEmail, contactEmail);

        return "redirect:/transfer?page=" + page;
    }

    @GetMapping("/addConnection")
    public String addConnection(Model model, String contactEmail) {
        logger.debug("This GetMapping methode addConnection starts here");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String appUserEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(appUserEmail);

        String userName = appUser.getFirstName() + " " + " " +appUser.getLastName();

        model.addAttribute("userEmail", appUserEmail);
        model.addAttribute("contactEmail", contactEmail);
        model.addAttribute("userName", userName);

        return "formAddConnection";
    }

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

        Set<AppUser> appUserBuddyList = appUserPay.getContacts();

        Page<Transaction> listTransaction = transactionRepository.findAllBySource(PageRequest.of(page, size), appUserPay);

        model.addAttribute("totalPage", listTransaction.getTotalPages());
        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name_user", name);
        model.addAttribute("userBuddyList", appUserBuddyList);
        model.addAttribute("userEmail", appUserEmail);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("buddyEmail", buddyEmail);

        return "transfer";
    }

    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

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
}
