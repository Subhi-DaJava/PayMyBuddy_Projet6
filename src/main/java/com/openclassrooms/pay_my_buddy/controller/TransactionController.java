package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.TransactionDTO;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
/*@RequestMapping("/pay-my-buddy")*/
public class TransactionController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/send-money")
    public String sendMoneyToBuddy(Model model,
                                   String userEmail,
                                  String buddyEmail,
                                   double amount,
                                 String description,
                                   @RequestParam(defaultValue = "0") int page) {
        //User connection = userService.findUserByEmail(buddyEmail);
      /*  User user = userService.findUserByEmail(userEmail);
        double userBalance = user.getBalance();

        if (buddyEmail == null || amount <= 0 || buddyEmail == null || userBalance > amount) {
            return "error/Bad_Operation";
        }*/
        model.addAttribute("buddyEmail", buddyEmail);
        transactionService.sendMoneyToBuddy(userEmail, buddyEmail, amount, description);
        return "redirect:/transfer?userEmail="+userEmail+"&page="+page;
    }

}
