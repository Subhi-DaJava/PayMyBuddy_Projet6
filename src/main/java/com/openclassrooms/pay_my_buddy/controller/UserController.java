package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import com.openclassrooms.pay_my_buddy.service.UserService;
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

import java.util.Set;

@Controller
@Transactional
/*@RequestMapping("/pay-my-buddy")*/
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/saveUser")
    public String saveUser(Model model){
        model.addAttribute("newUser", new AppUser());
        return "save-user-form";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(Model model,
                           AppUser appUser,
                           @RequestParam(defaultValue = "0") int page) throws EmailNotNullException {
        logger.debug("This saveUser starts here !!");

        //TODO: (@ModelAttribute("appUser") AppUser appUser)

        if (appUser.getEmail() == null || appUser.getPassword() == null) {
            logger.debug("UserEmail should not be null");
            return "save-user-form";
        }
        userService.saveUser(appUser);

        model.addAttribute("userEmail", appUser.getEmail());
        model.addAttribute("page", page);

        return "redirect:/transfer?page=" + page;

    }

    @PostMapping("/addBuddy")
    public String addBuddy(Model model,
                           @ModelAttribute("userEmail") String userEmail,
                           @ModelAttribute("contactEmail") String contactEmail,
                           @RequestParam(defaultValue = "0") int page) {
        //TODO: load AppUser

        logger.debug("This method addContactToUser(from UserController) starts here !!");

        AppUser appUserBuddy = userService.findAppUserByEmail(contactEmail);


        if(contactEmail == null || appUserBuddy == null){
            logger.debug("UserBuddyEmail={} should not be null or userBuddy doesn't exist in DB which the email.(from UserController)", contactEmail);
            return "error/Bad_Operation";
        }

        userService.addAppUserToContact(userEmail, contactEmail);
        logger.info("UserEmail={} successfully added the userBuddy={}", userEmail, contactEmail);

        return "redirect:/transfer?page=" + page;
    }

    @GetMapping("/addConnection")
    public String addConnection(Model model,
                                @ModelAttribute("contactEmail") String contactEmail) {
        logger.debug("This GetMapping methode addConnection starts here");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String appUserEmail = authentication.getName();

        model.addAttribute("userEmail", appUserEmail);
        model.addAttribute("contactEmail", contactEmail);

        return "formAddConnection";
    }

    @GetMapping("/transfer")
    public String showPageTransfer(Model model,
                                   @ModelAttribute("userEmail") String userEmail,
                                   @ModelAttribute("amount") String amount,
                                   @ModelAttribute("description") String description,
                                   @ModelAttribute("buddyEmail") String buddyEmail,
                                   @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "size", defaultValue = "3", required = false) int size
                                   ) {
        logger.debug("This showPageTransfer starts here");
        
        //TODO: load user by userEmail and transaction

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String appUserEmail = authentication.getName();
        AppUser appUserPay = userService.findAppUserByEmail(appUserEmail);
        String name = appUserPay.getFirstName() + " " + " " + appUserPay.getLastName();

        Set<AppUser> appUserBuddyList = appUserPay.getContacts();

        Page<Transaction> listTransaction = transactionRepository.findAll(PageRequest.of(page, size));


        model.addAttribute("totalPage", listTransaction.getTotalPages());
        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name_user", name);
        model.addAttribute("userBuddyList", appUserBuddyList);
        model.addAttribute("userEmail", appUserEmail);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("buddyEmail",buddyEmail);

        return "transfer";
    }

    @GetMapping("/home")
    public String homePage(){
        return "home";
    }



}
