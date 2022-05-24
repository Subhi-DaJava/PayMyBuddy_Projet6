package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.exception.EmailNotNullException;
import com.openclassrooms.pay_my_buddy.exception.UserExistingException;
import com.openclassrooms.pay_my_buddy.model.User;
import com.openclassrooms.pay_my_buddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/users/login")
    public ResponseEntity<User> getSingIn(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password){
        User user = userService.login(email, password);
        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@Valid @PathVariable String email){
        User user = userService.findUserByEmail(email);
        if(user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) throws EmailNotNullException {
        logger.debug("This saveUser starts here !!");
        if(user.getEmail() == null || user.getUserName() == null)
            throw new UserExistingException("This user already exist in the DB.");

        User userCreated = userService.saveUser(user);
        if (userCreated != null){
            logger.info("Successfully save this user with userEmail ["+user.getEmail()+"]");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{userId}")
                    .buildAndExpand(userCreated.getUserId())
                    .toUri();
            return ResponseEntity.created(location).body(userCreated);
        }
        else {
            logger.debug("Bad request, check all fields !!");
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/users/{userId}/add-contact/{contactEmail}")
    public ResponseEntity<Boolean> addContactToUser(@Valid @PathVariable Integer userId, @PathVariable String contactEmail){
        logger.debug("This method addContactToUser starts here !!");
        if (userService.addUserToContact(userId, contactEmail)){
            logger.info("This contactEmail ["+contactEmail+"] is successfully added to this userEmail ["+ userId +"]");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/user_id/contact_id")
                    .buildAndExpand(true)
                    .toUri();
            return ResponseEntity.created(location).build();
        }
        else if(contactEmail == null){
            logger.debug("buddyEmail should not be null or be empty neither !!");
            throw new EmailNotNullException("Not null or not empty !!");
        }
        else {
            logger.debug("This contactEmail ["+contactEmail+"] could not be added to this userEmail +["+ userId +"]");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/contacts/{userId}")
    private ResponseEntity<Set<User>> getAllContactsByUser(@PathVariable Integer userId){
        Set<User> allContactsByUser = userService.getAllContactsByUser(userId);
        if (allContactsByUser.isEmpty()){
            return ResponseEntity.ok().body(new HashSet<>());
        }
        return ResponseEntity.ok().body(allContactsByUser);
    }

}
