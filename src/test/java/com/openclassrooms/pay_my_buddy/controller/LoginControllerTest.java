package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LoginController.class)
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private UserBankAccountService userBankAccountService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loginTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("Login with Google")));

    }

    @Test
    void indexTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/home"));
    }

    @Test

    void homePageTest() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    void signupGetMappingTest() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(content().string(containsString("Save New AppUser")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("appUser"));
    }


}