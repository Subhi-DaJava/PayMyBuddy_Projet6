package com.openclassrooms.pay_my_buddy.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin/dashboard")
    String adminDashboard(){

        return "dashboard";
    }

    @GetMapping("/admin/addRoleToUser")
    String addRoleToUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return "addRoleToUser";
    }
}
