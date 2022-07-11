package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.BankAccountDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
import com.openclassrooms.pay_my_buddy.service.UserBankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserBankAccountController.class)
class UserBankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private UserBankAccountService userBankAccountService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private AppUser user1;
    private Role role;
    private List<Role> roles;
    private UserBankAccount userBankAccount;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        passwordEncoder = new BCryptPasswordEncoder();
        role = new Role();
        String roleName = "USER";
        role.setRoleName(roleName);
        roles = new ArrayList<>();
        roles.add(role);

        String userEmail = "email@gmail.com";
        user1 = new AppUser();
        user1.setEmail(userEmail);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(100);
        user1.setRoles(roles);
        user1.setPassword(passwordEncoder.encode("12345"));

        userBankAccount = new UserBankAccount();
        userBankAccount.setBankName("UserBank_LCL");
        userBankAccount.setBankLocation("Paris15");
        userBankAccount.setCodeIBAN("FR75BLCL");
        userBankAccount.setCodeBIC("CODEBIC");

    }

    @Test
    @WithMockUser
    void myBankInfoTest() throws Exception {
        // Arrange
        user1.setUserBankAccount(userBankAccount);
        BankAccountDTO bankAccountInfo = new BankAccountDTO();
        bankAccountInfo.setBankLocation("Paris15");
        bankAccountInfo.setBankName("UserBank_LCL");
        bankAccountInfo.setCodeIBAN("FR75BLCL");
        bankAccountInfo.setCodeBIC("CODEBIC");

        // Act
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountService.bankAccountInfo(user1)).thenReturn(bankAccountInfo);

        mockMvc.perform(get("/myBankAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-bank-info"));
    }

    @Test
    @WithMockUser
    void userHasAnyBankAccountTest() throws Exception {
        // Arrange
        // Act
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountService.bankAccountInfo(user1)).thenReturn(null);

        mockMvc.perform(get("/myBankAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBankAccount"));
    }

    @Test
    @WithMockUser
    void addBankAccountTest() throws Exception {
        // Arrange
        // Act
        mockMvc.perform(get("/addBankAccount")
                        .param("userEmail", "email@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userEmail", "email@gmail.com"))
                .andExpect(view().name("addBankAccount"));

    }

    @Test
    @WithMockUser
    void addBankAccountToPMBPostMappingUserBankAccountAlreadyExistingTest() throws Exception {
        // Arrange
        user1.setUserBankAccount(userBankAccount);
        userBankAccount.setAppUser(user1);
        // Act
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);

        mockMvc.perform(post("/addBankAccount")
                        .param("userEmail", "email@gmail.com")
                        .param("bankName", "UserBank_LCL")
                        .param("bankLocation", "Paris15")
                        .param("codeIBAN", "FR75BLCL")
                        .param("codeBIC", "CODEBIC"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBankAccount"))
                .andExpect(content().string(containsString("This user has already associated with a bank account")));
    }
    @Test
    @WithMockUser
    void addBankAccountToPMBPostMappingCodeIBANAlreadyTakenTest() throws Exception {
        // Arrange
        UserBankAccount userBankAccount2 = new UserBankAccount();
        userBankAccount2.setAppUser(new AppUser());
        userBankAccount2.setBankName("UserBank_LCL");
        userBankAccount2.setBankLocation("Paris15");
        userBankAccount2.setCodeIBAN("FRIBAN76");
        userBankAccount2.setCodeBIC("CODEBIC75");

        // Act
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountService.findByCodeIBAN(anyString())).thenReturn(userBankAccount2);

        mockMvc.perform(post("/addBankAccount")
                        .param("userEmail", "email@gmail.com")
                        .param("bankName", "UserBank_CIC")
                        .param("bankLocation", "Paris16")
                        .param("codeIBAN", "FRIBAN76")
                        .param("codeBIC", "CODEBIC75"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBankAccount"))
                .andExpect(content().string(containsString("This codeIBAN already associated!")));
    }

    @Test
    @WithMockUser
    void addBankAccountToPMBPostMappingTest() throws Exception {
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        doNothing().when(userBankAccountService).addBankAccountToPayMyBuddy(
                "email@gmail.com",
                "UserBank_LCL",
                "Paris15",
                "FR75BLCL",
                "CODEBIC");

        mockMvc.perform(post("/addBankAccount")
                        .param("userEmail", "email@gmail.com")
                        .param("bankName", "UserBank_LCL")
                        .param("bankLocation", "Paris15")
                        .param("codeIBAN", "FR75BLCL")
                        .param("codeBIC", "CODEBIC"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));

    }
}