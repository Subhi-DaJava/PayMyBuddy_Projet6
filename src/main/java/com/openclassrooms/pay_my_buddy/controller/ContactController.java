package com.openclassrooms.pay_my_buddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ContactController affiche la page des informations et coordonnées concernant l'application
 */

@Controller
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @GetMapping("/contact")
    public String showContact(Model model){
        logger.debug("This showContact method(from ContactController starts here.)");
        return "contact";
    }

}
