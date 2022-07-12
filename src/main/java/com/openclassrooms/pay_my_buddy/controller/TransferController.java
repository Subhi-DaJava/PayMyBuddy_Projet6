package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.dto.TransferBetweenBankAndPayMyBuddyDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;

import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.TransferService;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional
public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);
    @Autowired
    private TransferService transferService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserBankAccountService userBankAccountService;

    @PostMapping("/transfer/pmb-bank")
    public String transferMoneyToPayMyBuddyUser(
            @RequestParam(name = "amount") double amount,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "operationType") OperationType operationType,
            @RequestParam(defaultValue = "0") int page) {
        logger.debug("This transferMoneyToPayMyBuddyUser methode starts here.(TransferController)");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        userBankAccountService.transferBetweenBankAndPMB(userEmail, amount, description, operationType);
        logger.info("This operation transfer money to PayMyBuddy is successful!(from transferMoneyToPayMyBuddy)");

        return "redirect:/transfer?page=" + page;
    }

    @GetMapping("/transfer/pmb-bank")
    public String transferBetweenBankAndPMB(Model model,
                                            String amount,
                                            String description,
                                            OperationType operationType){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);

        String userName = appUser.getFirstName() + " " + " " +appUser.getLastName();

        String BANKtoPMB = String.valueOf(OperationType.CREDIT);
        String PMBtoBANK = String.valueOf(OperationType.DEBIT);
        List<String> operationTypes = new ArrayList<>();
        operationTypes.add(BANKtoPMB);
        operationTypes.add(PMBtoBANK);

        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("operationType", operationType);
        model.addAttribute("operationTypes", operationTypes);
        model.addAttribute("name_user", userName);

        return "transfersBetweenBankAndPMB";
        //TODO: améliorer GetMapping
    }

    @GetMapping("/myTransfers")
    public String showMyTransfers(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser appUser = securityService.loadAppUserByUserEmail(userEmail);
        String user_name = appUser.getFirstName() + " " + appUser.getLastName();

        List<TransferBetweenBankAndPayMyBuddyDTO> allMyTransfers = transferService.findAllTransfersByUser(appUser);

        model.addAttribute("allTransfers", allMyTransfers);
        model.addAttribute("name_user", user_name);

        return "myTransfers";
    }



}
