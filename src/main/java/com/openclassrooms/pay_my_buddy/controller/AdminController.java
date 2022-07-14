package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Admin controller gère afficher le tableau de bord admin et l'admin peut rajouter un ROLE à un user
 */
@Controller
@Transactional
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private SecurityService securityService;

    public AdminController(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Affiche le tableau de board d'admin
     * @param model
     * @return
     */
    @GetMapping("/admin/dashboard")
    String adminDashboard(Model model){
        logger.info("This adminDashboard method starts here(AdminController)");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        AppUser admin = securityService.loadAppUserByUserEmail(adminEmail);

        model.addAttribute("admin", admin);

        return "dashboard";
    }

    /**
     * Affiche le formulaire pour rajout d'un ROLE à un User
     * @param model
     * @param userEmail
     * @param roleName
     * @return
     */

    @GetMapping("/admin/addRoleToUser")
    String addRoleToUser(Model model,
                         String userEmail,
                         String roleName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        AppUser admin = securityService.loadAppUserByUserEmail(adminEmail);

        model.addAttribute("userEmail", userEmail); //TODO: remplace avec userID
        model.addAttribute("roleName", roleName);
        model.addAttribute("admin", admin);

        return "addRoleToUser";
    }

    /**
     * Rajoute un ROLE à un Utilisateur
     * @param email
     * @param roleName
     * @return
     */

    @PostMapping("/admin/addRoleToUser")
    public String addRoleToUser(@RequestParam(name = "userEmail") String email,
                                @RequestParam(name = "roleName") String roleName){

        securityService.addRoleToUser(email,roleName);

        return "redirect:/admin/dashboard";
    }

}
