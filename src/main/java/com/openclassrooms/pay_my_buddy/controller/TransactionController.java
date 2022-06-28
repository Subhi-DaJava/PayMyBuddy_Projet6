package com.openclassrooms.pay_my_buddy.controller;


import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
/*@RequestMapping("/pay-my-buddy")*/
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private SecurityService securityService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/user/send-money")
    public String sendMoneyToBuddy(Model model,
                                   @ModelAttribute("userEmail") String userEmail,
                                   @ModelAttribute("buddyEmail") String buddyEmail,
                                   @ModelAttribute("amount") double amount,
                                   @ModelAttribute("description") String description,
                                   @RequestParam(defaultValue = "0") int page) {
        logger.debug("This send-money in TransactionController starts here");

        AppUser connection = securityService.loadAppUserByUserEmail(buddyEmail);
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        double userBalance = appUser.getBalance();

        if (buddyEmail == null || amount <= 0 || userBalance < amount || connection == null) {
            logger.debug("UserEmail={} should not bu null or buddyEmail={} neither, or amount should be positif number, userBalance greater than amount," +
                    " and userBuddy should be in DB", userEmail, buddyEmail);
            return "error/Bad_Operation";
        }
        model.addAttribute("buddyEmail", buddyEmail);

        transactionService.sendMoneyToBuddy(userEmail, buddyEmail, amount, description);

        return "redirect:/transfer?page="+page;
    }

}
