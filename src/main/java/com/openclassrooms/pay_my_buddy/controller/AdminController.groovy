package com.openclassrooms.pay_my_buddy.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminController {
    @GetMapping("/admin/dashboard")
    public String adminDashboard(){

        return "dashboard";
    }

}
