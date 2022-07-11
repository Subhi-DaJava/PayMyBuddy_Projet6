package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ContactController.class)
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Test
    public void showContactTest() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
    }
}