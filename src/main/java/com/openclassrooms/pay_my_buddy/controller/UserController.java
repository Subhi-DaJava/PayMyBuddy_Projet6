package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/users/login")
    public User getSingIn(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password){
        return userService.login(email, password);
    }

    @GetMapping("/users/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }


    @PostMapping("/users")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PostMapping("/users/{userEmail}/{contactEmail}")
    public void addContactToUser(@PathVariable String userEmail, @PathVariable String contactEmail){
        userService.addUserToContact(userEmail, contactEmail);
    }


}
