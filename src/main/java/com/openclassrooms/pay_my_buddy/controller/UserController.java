package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
        model.addAttribute("newUser", new User());
        return "save-user-form";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(Model model,
                           User user,
                           @RequestParam(defaultValue = "0") int page) throws EmailNotNullException {
        logger.debug("This saveUser starts here !!");
        if (user.getEmail() == null || user.getPassword() == null) {
            logger.debug("UserEmail should not be null");
            return "save-user-form";
        }
        userService.saveUser(user);

        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("page", page);

        return "redirect:/transfer?userEmail=" + user.getEmail() + "&page=" + page;

    }

    @PostMapping("/addBuddy")
    public String addBuddy(Model model,
                          String userEmail,
                          String contactEmail,
                           @RequestParam(defaultValue = "0") int page) {

        logger.debug("This method addContactToUser starts here !!");

        User userBuddy = userService.findUserByEmail(contactEmail);
        if(contactEmail == null || userBuddy == null){
            logger.debug("UserBuddy={} should not be null or userBuddy doesn't exist in DB wich the email", contactEmail);
            return "error/Bad_Operation";
        }

        userService.addUserToContact(userEmail, contactEmail);
        logger.info("UserEmail={} seuccessfully added the userBuddy={}", userEmail, contactEmail);

        return "redirect:/transfer?userEmail=" + userEmail + "&page=" + page;
    }

    @GetMapping("/addConnection")
    public String addConnection(Model model,
                                @RequestParam(name = "userEmail", defaultValue = "userEmail") String userEmail,
                                @RequestParam(name = "contactEmail", defaultValue = "contactEmail") String contactEmail) {
        logger.debug("This GetMapping methode addConnection starts here");


        model.addAttribute("userEmail", userEmail);
        model.addAttribute("contactEmail", contactEmail);


        return "formAddConnection";
    }

    @GetMapping("/transfer")
    public String showPageTransfer(Model model,
                                   @RequestParam(name = "userEmail", value = "userEmail") String userEmail,
                                   String amount,
                                   String description,
                                   String buddyEmail,
                                   @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "size", defaultValue = "3", required = false) int size
                                   ) {
        logger.debug("This showPageTransfer starts here");

        User userPay = userService.findUserByEmail(userEmail);
        Set<User> userBuddyList = userPay.getContacts();


        Page<Transaction> listTransaction = transactionRepository.findTransactionByUserPay(userPay, PageRequest.of(page, size));



     /*   List<User> buddyList = new ArrayList<>();

        List<Transaction> transList = listTransaction.getContent();
        for(Transaction trans : transList){
            User checkBuddy = userService.findUserByEmail(trans.getBuddyEmail());
            buddyList.add(checkBuddy);
        }

*/
     /*   List<TransactionDTO> newDto = new ArrayList<>();
        for (Transaction dto : listTransaction){
            TransactionDTO dtoCreate = new TransactionDTO();
            dtoCreate = transactionService.

        }*/


        /*List<TransactionDTO> transactionDTOList = transactionService.findAllTransactionByUser(userEmail);*/

        /*model.addAttribute("transactionDTOList", transactionDTOList);*/

        model.addAttribute("totalPage", listTransaction.getTotalPages());
        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);

        model.addAttribute("userBuddyList", userBuddyList);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("buddyEmail",buddyEmail);


        //model.addAttribute("buddyList", buddyList);

        return "transfer";
    }


    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "/login";
        }
        return "redirect:/transfer";
    }

}
