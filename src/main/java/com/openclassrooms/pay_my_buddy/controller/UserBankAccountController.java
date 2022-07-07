package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.BankAccountDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Transactional
public class UserBankAccountController {
    private static final Logger logger = LoggerFactory.getLogger(UserBankAccountController.class);
    private SecurityService securityService;
    private UserBankAccountService userBankAccountService;

    public UserBankAccountController(SecurityService securityService, UserBankAccountService userBankAccountService) {
        this.securityService = securityService;
        this.userBankAccountService = userBankAccountService;
    }

    @GetMapping("/myBankAccount")
    public String myBankInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String appUserEmail = authentication.getName();
        AppUser appUser = securityService.loadAppUserByUserEmail(appUserEmail);

        if(appUser.getUserBankAccount() == null){
           String anyBankError = "This User has any bank associated, please add a bank account!";
           model.addAttribute("anyBankError", anyBankError);
            return "addBankAccount";
        }
        String user_name = appUser.getFirstName() + " " + appUser.getLastName();


        BankAccountDTO bankAccountInfo = userBankAccountService.bankAccountInfo(appUser);

        model.addAttribute("bankAccountInfo", bankAccountInfo);
        model.addAttribute("user_name",user_name);

        return "my-bank-info";
    }

    @GetMapping("/addBankAccount")
    public String addBankAccount(Model model,
                                 String userEmail,
                                 String bankName,
                                 String bankLocation,
                                 String codeIBAN,
                                 String codeBIC){
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("bankName", bankName);
        model.addAttribute("bankLocation", bankLocation);
        model.addAttribute("codeIBAN", codeIBAN);
        model.addAttribute("codeBIC", codeBIC);

        return "addBankAccount";
    }
    @PostMapping("/addBankAccount")
    public String addBankAccountToPMB(Model model,
                                      @RequestParam(name = "userEmail") String  userEmail,
                                      @RequestParam(name = "bankName") String bankName,
                                      @RequestParam(name = "bankLocation") String bankLocation,
                                      @RequestParam(name = "codeIBAN") String codeIBAN,
                                      @RequestParam(name = "codeBIC") String codeBIC){
        logger.debug("This addBankAccountToPMB method(from UserBankAccountController) starts here.");

        UserBankAccount check = userBankAccountService.findByCodeIBAN(codeIBAN);

        if (securityService.loadAppUserByUserEmail(userEmail) != null){
            if (securityService.loadAppUserByUserEmail(userEmail).getUserBankAccount() != null){
                String bankAssociated = "This user has already associated with a bank account";
                model.addAttribute("bankAssociated", bankAssociated);
                return "addBankAccount";
            }
        }
        if (check != null){
            logger.debug("This bankAccount with the codeIBAN={} already exists in DB !!(from UserBankAccountServiceImpl)", codeIBAN);
            String codeIBANExit = "This codeIBAN already associated!";
            model.addAttribute("codeIBANExit", codeIBANExit);
            return "addBankAccount";
        }
        if (securityService.loadAppUserByUserEmail(userEmail) == null){
            logger.debug("This addBankAccountToPMB(form UserBankAccountController), email should not be null.");
            String userNotExist = "This user with this email address doesn't exit in DB!";
            model.addAttribute("userNotExist", userNotExist);
            return "addBankAccount";
        }
        userBankAccountService.addBankAccountToPayMyBuddy(
                userEmail,
                bankName,
                bankLocation,
                codeIBAN,
                codeBIC);
        return "redirect:/login";
    }





}
