package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/users/login")
    public ResponseEntity<User> getSingIn(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password){
        User user = userService.login(email, password);
        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        User user = userService.findUserByEmail(email);
        if(user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(user);
    }


    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws EmailNotNullException {
        User userCreated = userService.saveUser(user);
        if (userCreated != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{userId}")
                    .buildAndExpand(userCreated.getUserId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }
        else if(user.getEmail().isEmpty()){
            throw new EmailNotNullException("Email should not be null !! !");
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/users/{userEmail}/{contactEmail}")
    public ResponseEntity<Boolean> addContactToUser(@PathVariable String userEmail, @PathVariable String contactEmail){
        if (userService.addUserToContact(userEmail, contactEmail)){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/user_id/contact_id")
                    .buildAndExpand(true)
                    .toUri();
            return ResponseEntity.created(location).build();
        }else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/contacts/{userId}")
    private ResponseEntity<Set<User>> getAllContactsByUser(@PathVariable Integer userId){
        Set<User> allContactsByUser = userService.getAllContactsByUser(userId);
        if (allContactsByUser.isEmpty()){
            Set<User> contactsEmpty = new HashSet<>();
            return ResponseEntity.ok().body(contactsEmpty);
        }
        return ResponseEntity.ok().body(allContactsByUser);
    }


}
