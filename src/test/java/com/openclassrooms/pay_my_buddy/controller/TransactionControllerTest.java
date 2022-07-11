package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.Payment;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;
    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebApplicationContext context;
    private AppUser user1;
    private AppUser user2;
    private Role role;
    private Set<AppUser> connections;
    private List<Role> roles;

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
        user1.setBalance(500);
        user1.setRoles(roles);
        user1.setPassword(passwordEncoder.encode("12345"));

        String buddyEmail = "buddy@gmail.com";
        user2 = new AppUser();
        user2.setEmail(buddyEmail);
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setBalance(0.0);
        user2.setRoles(roles);
        user2.setPassword(passwordEncoder.encode("12345"));

        connections = new HashSet<>();
    }
    @Test
    @WithMockUser
    void sendMoneyToBuddyPostMappingTest() throws Exception {
        // Arrange
        // Act
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);

        doNothing().when(transactionService).
                sendMoneyToBuddy(
                        "email@gmail.com",
                        "buddy@gmail.com",
                        100,
                        "descriptionTest");

        mockMvc.perform(post("/user/send-money")
                .param("buddyEmail", "buddy@gmail.com")
                .param("amount", String.valueOf(100))
                .param("description", "descriptionTest")
                .param("page", String.valueOf(0))
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/transfer?page=0"));


    }

    @Test
    @WithMockUser
    void myTransactionsTest() throws Exception {
        // Arrange
        user1.getConnections().add(user2);
        Payment payment = new Payment();
        payment.setEmail("buddy@gmail.com");
        payment.setAmont(100);
        payment.setLocalDate(LocalDate.now());
        payment.setDescription("test");
        payment.setGetPayedName("FirstName"+ " " + " LastName");
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        // Act
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(transactionService.findTransactionsBySource(any(AppUser.class))).thenReturn(payments);

        mockMvc.perform(get("/myPayments"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("paymentsByUser", payments))
                .andExpect(view().name("myPayments"));


    }
}