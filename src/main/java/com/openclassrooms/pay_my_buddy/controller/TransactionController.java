package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.Payment;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TransactionController, la méthode sendMoneyToBuddy qui fait la transaction, un User peut envoie l'argent à un des amis,
 * la méthode myTransactions s'occupe de charger toutes les transactions d'un user
 */
@Controller
@Transactional
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private SecurityService securityService;
    @Autowired
    private TransactionService transactionService;

    /**
     * Envoyer l'argent vers un autre user
     * @param buddyEmail
     * @param amount
     * @param description
     * @param page
     * @return
     */
    @PostMapping("/user/send-money")
    public String sendMoneyToBuddy(@RequestParam(name = "buddyEmail") String buddyEmail,
                                   @RequestParam(name = "amount") double amount,
                                   @RequestParam(name = "description") String description,
                                   @RequestParam(defaultValue = "0") int page) {
        logger.debug("This send-money in TransactionController starts here");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser connection = securityService.loadAppUserByUserEmail(buddyEmail);
        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        double userBalance = appUser.getBalance();

        if (buddyEmail == null || amount <= 0 || userBalance < amount || connection == null) {
            logger.debug("UserEmail={} should not bu null or buddyEmail={} neither, or amount should be positif number, userBalance greater than amount," +
                    " and userBuddy should be in DB", userEmail, buddyEmail);
            return "error/Bad_Operation";
        }

        transactionService.sendMoneyToBuddy(userEmail, buddyEmail, amount, description);

        return "redirect:/transfer?page="+page;
    }

    /**
     * Renvoie toutes les transactions d'un user
     * @param model
     * @return
     */
    @GetMapping("/myPayments")
    public String myTransactions(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);
        String user_name = appUser.getFirstName() + " " + appUser.getLastName();

        List<Payment> paymentsByUser = transactionService.findTransactionsBySource(appUser);

        model.addAttribute("paymentsByUser", paymentsByUser);

        model.addAttribute("user_name", user_name);

        return "myPayments";
    }

}
