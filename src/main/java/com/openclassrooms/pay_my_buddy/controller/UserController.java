package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.TransactionDTO;
import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.TransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
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

    @PostMapping("/admin/saveUser")
    public String saveUser(@Valid @RequestBody User user) throws EmailNotNullException {
        logger.debug("This saveUser starts here !!");
        if (user.getEmail() == null || user.getPassword() == null) {
            logger.debug("UserEmail should not be null");
            return "redirect:/formSaveUser";
        }
        userService.saveUser(user);
        return "redirect:/user/home";

    }

/*    @GetMapping("/transfer_page")
    public String showPageTransfer(Model model,
                                   @RequestParam String email){
        User user = userService.findUserByEmail(email);
        Set<User> userContact = user.getContacts();
        List<Transaction> transactions = user.getTransactions();
        model.addAttribute("userContact", userContact);
        model.addAttribute("transactions", transactions);
        return "transfer";
    }*/



    @PostMapping("/addBuddy")
    public String addBuddy(Model model,
                           @RequestParam(value = "userEmail", required = false) String userEmail,
                           @RequestParam(value = "contactEmail", required = false) String contactEmail) {

        logger.debug("This method addContactToUser starts here !!");
        userService.addUserToContact(userEmail, contactEmail);

        return "redirect:/transfer?userEmail="+userEmail;
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


/*    @GetMapping("/user/contacts/{userId}")
    private ResponseEntity<Set<User>> getAllContactsByUser(@PathVariable Integer userId) {
        Set<User> allContactsByUser = userService.getAllContactsByUser(userId);
        if (allContactsByUser.isEmpty()) {
            return ResponseEntity.ok().body(new HashSet<>());
        }
        return ResponseEntity.ok().body(allContactsByUser);
    }*/

 /*   @GetMapping("/transfer")
    public String transfer(Model model,
                           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                           @RequestParam(name = "size", defaultValue = "3", required = false) int size){
        model.addAttribute("page",page);
        model.addAttribute("size", size);
        return "transfer";
    }
*/
    @GetMapping("/showTransfer")
    public String transfer(@RequestParam(name = "userEmail", required = false) String userEmail,
                           @RequestParam int page,
                           @RequestParam int size)
     {
        return "redirect:/transfer?email="+userEmail+"&page="+page+"&size="+size;
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

        List<TransactionDTO> transactionDTOList = transactionService.findAllTransactionByUser(userPay.getEmail());
        model.addAttribute("transactionDTO", transactionDTOList);

        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);

        model.addAttribute("userBuddyList", userBuddyList);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
        model.addAttribute("buddyEmail",buddyEmail);

        return "transfer";
    }

  /*  public String allTransaction(Model model,
                                 @RequestParam User user,
                                 @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                 @RequestParam(name = "size", defaultValue = "3", required = false) int size) {

        Page<Transaction> listTransaction = transactionRepository.findTransactionByUserPay(user, PageRequest.of(page, size));
        List<TransactionDTO> transactionDTOList = transactionService.findAllTransactionByUser(user.getEmail());

        model.addAttribute("transactionDTO", transactionDTOList);
        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "redirect:/transfer?page=" + page + "&size=" + size;*/




/*    @GetMapping("/transfer/{userEmail}")
    public String ShowPageTransferAndTransferContacts(Model model,
                                  @PathVariable(name = "userEmail", required = false) String userEmail,
                                  @RequestParam(name = "buddyEmail", required = false, defaultValue = "buddyEmail") String buddyEmail,
                                   @RequestParam(name = "amount", defaultValue = "amount") String amount,
                                   @RequestParam(name = "description", defaultValue = "description") String description) {

        User userPay = userService.findUserByEmail(userEmail);
        Set<User> userBuddyList = userPay.getContacts();

        User userBuddy = userService.findUserByEmail(buddyEmail);

        model.addAttribute("userEmail", userEmail);

        model.addAttribute("userPay", userPay);
        model.addAttribute("userBuddy", userBuddy);
        model.addAttribute("userBuddyList", userBuddyList);


        model.addAttribute("buddyEmail", buddyEmail);
        model.addAttribute("description", description);
        model.addAttribute("amount", amount);

        return "redirect:/transfer?userEmail="+userEmail;
    }*/

    @GetMapping("/send-Money")
    public String sendMoneyToBuddy(@RequestParam String userEmail,
                                   @RequestParam String buddyEmail,
                                   @RequestParam double amount,
                                   @RequestParam String description, Model model) {

        /*model.addAttribute("userEmail", userEmail);
        model.addAttribute("buddyEmail", buddyEmail);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);
*/
        return "redirect:/transfer?userEmail="+userEmail;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
