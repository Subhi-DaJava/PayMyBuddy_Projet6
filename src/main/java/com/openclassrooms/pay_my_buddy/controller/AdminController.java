package com.openclassrooms.pay_my_buddy.controller;


import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private SecurityService securityService;

    public AdminController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/admin/dashboard")
    String adminDashboard(){

        return "dashboard";
    }

    @GetMapping("/admin/addRoleToUser")
    String addRoleToUser(Model model,
                         String userEmail,
                         String roleName){

        model.addAttribute("userEmail", userEmail);
        model.addAttribute("roleName", roleName);

        return "addRoleToUser";
    }

    @PostMapping("/admin/addRoleToUser")
    public String addRoleToUser(@RequestParam(name = "userEmail") String email,
                                @RequestParam(name = "roleName") String roleName){

        securityService.addRoleToUser(email,roleName);

        return "redirect:/admin/dashboard";
    }

}
