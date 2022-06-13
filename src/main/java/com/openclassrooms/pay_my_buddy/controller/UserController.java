package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Controller
@Transactional
@RequestMapping("/pay-my-buddy")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/login")
    public ResponseEntity<User> getSingIn(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        User user = userService.login(email, password);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) throws EmailNotNullException {

        logger.debug("This saveUser starts here !!");
        if (user.getEmail() == null || user.getUserName() == null) {
            throw new UserExistingException("This user already exist in the DB.");
        }

        User userCreated = userService.saveUser(user);
        if (userCreated != null) {
            logger.info("Successfully save this user with userEmail [" + user.getEmail() + "]");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{userId}")
                    .buildAndExpand(userCreated.getUserId())
                    .toUri();
            return ResponseEntity.created(location).body(userCreated);
        } else {
            logger.debug("Bad request, check all fields !!");
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/addBuddy")
    public String addBuddy(Model model,
                           @RequestParam(value = "user_Id", required = false) Integer userId,
                           @RequestParam(value = "contact_Email", required = false) String contactEmail) {

        logger.debug("This method addContactToUser starts here !!");
        userService.addUserToContact(userId, contactEmail);

        model.addAttribute("user_Id", userId);
        model.addAttribute("contact_Email", contactEmail);

        return "redirect:/pay-my-buddy/transfer";
    }

    @GetMapping("/addConnection")
    public String addConnection() {

        return "formAddConnection";
    }


    @GetMapping("/user/contacts/{userId}")
    private ResponseEntity<Set<User>> getAllContactsByUser(@PathVariable Integer userId) {
        Set<User> allContactsByUser = userService.getAllContactsByUser(userId);
        if (allContactsByUser.isEmpty()) {
            return ResponseEntity.ok().body(new HashSet<>());
        }
        return ResponseEntity.ok().body(allContactsByUser);
    }

/*    @GetMapping("/transfer")
    public String transfer(Model model,
                           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                           @RequestParam(name = "size", defaultValue = "3", required = false) int size){
        model.addAttribute("page",page);
        model.addAttribute("size", size);
        return "transfer";
    }*/

    @GetMapping("/transfer")
    public String transfer() {
        return "transfer";
    }


    @GetMapping("/transfer/{userId}")
    public String transferContacts(Model model, @PathVariable(name = "userId", required = false) Integer id) {

        Set<User> userContacts = userService.getAllContactsByUser(id);
        model.addAttribute("userId", id);
        model.addAttribute("contacts", userContacts);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String sendMoneyToBuddy(@RequestParam Integer payedId,
                                   @RequestParam String userName,
                                   @RequestParam double amount,
                                   @RequestParam String description, Model model) {

        if (payedId <= 0 || userName == null || amount <= 0) {
            throw new RuntimeException("Bad request !!");
        }

        transactionService.sendMoneyToBuddy(payedId, userName, amount, description);

        model.addAttribute("payedId", payedId);
        model.addAttribute("userName", userName);
        model.addAttribute("amount", amount);
        model.addAttribute("description", description);

        return "transfer";
    }

    @GetMapping("/sendMoney")
    public String sendMoney() {
        return "sendMoney";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
