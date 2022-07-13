package com.openclassrooms.pay_my_buddy.integrationTests;

import com.openclassrooms.pay_my_buddy.exception.PasswordNotMatchException;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private WebApplicationContext context;

    private final static String APP_USER = "laurentgina@gmail.com";
    private final static String BUDDY_USER = "test@gmail.com";

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }
    @Test
    void addBuddyNotExistingTest() throws Exception {
        mockMvc.perform(post("/addBuddy")
                        .with(user(APP_USER).password("12345"))
                        .param("buddyEmail", "BUDDY_USER@gmail.com")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/addBuddy"));
    }

    @Test
    void addBuddyTest() throws Exception {
        mockMvc.perform(post("/addBuddy")
                        .with(user(APP_USER).password("12345"))
                        .param("buddyEmail", BUDDY_USER))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/transfer?page=0"));

        assertThat(securityService.loadAppUserByUserEmail(APP_USER).getFirstName()).isEqualTo("Laurent");
        assertThat(securityService.loadAppUserByUserEmail(APP_USER).getBalance()).isEqualTo(3000);
    }
    @Test
    void addConnectionTest() throws Exception {
        mockMvc.perform(get("/addConnection")
                .with(user(APP_USER).password("12345"))
                .param("buddyEmail", BUDDY_USER))
                .andExpect(status().isOk())
                .andExpect(view().name("formAddConnection"));
    }

    @Test
    void editUserGetMappingTest() throws Exception {
        mockMvc.perform(get("/editUser")
                        .with(user(APP_USER).password("12345")))
                .andExpect(status().isOk())
                .andExpect(view().name("editUser"));

    }
    @Test
    void editProfilePostMappingTest() throws Exception {
        mockMvc.perform(post("/editUser")
                        .with(user(APP_USER).password("12345"))
                        .param("firstName","firstNameUpdated")
                        .param("lastName", "lastNameUpdated")
                        .param("email", APP_USER))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        assertThat(userRepository.findByEmail(APP_USER).getFirstName()).isEqualTo("firstNameUpdated");
    }

    @Test
    void editProfilePostMappingFailureTest() throws Exception {
        try {
            mockMvc.perform(post("/editUser")
                            .with(user(APP_USER).password("12345"))
                            .param("firstName","firstNameUpdated")
                            .param("lastName", "lastNameUpdated")
                            .param("email", ""))
                    .andExpect(status().is4xxClientError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                    .andExpect(view().name("Bad_Operation"));
        } catch (Exception e){
            e.getMessage();
        }

    }
    @Test
    void editProfilePostMappingWithEmailNullTest() throws Exception {
        try {
            mockMvc.perform(post("/editUser")
                            .with(user(APP_USER).password("12345"))
                            .param("firstName","firstNameUpdated")
                            .param("lastName", "lastNameUpdated")
                            .param("email", null))
                    .andExpect(status().is4xxClientError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                    .andExpect(view().name("Bad_Operation"));
        } catch (Exception e){
            e.getMessage();
        }

    }

    @Test
    void changePasswordGetMappingTest() throws Exception {
        mockMvc.perform(get("/changePassword")
                .with(user(APP_USER).password("12345"))
                        .param("password", "54321")
                        .param("rePassword", "54321"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePassword"));
    }

    @Test
    void updatePasswordPostMappingTest() throws Exception {
        mockMvc.perform(post("/changePassword")
                        .with(user(APP_USER).password("12345"))
                        .param("password", "54321")
                        .param("rePassword", "54321"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void updatePasswordPostMappingFailureTest() throws Exception {
       try {
           mockMvc.perform(post("/changePassword")
                           .with(user(APP_USER).password("12345"))
                           .param("password", "54321")
                           .param("rePassword", "5432"))
                   .andExpect(status().is4xxClientError())
                   .andExpect(result -> assertTrue(result.getResolvedException() instanceof PasswordNotMatchException));
       } catch (Exception e){
           e.getMessage();
       }
    }
}