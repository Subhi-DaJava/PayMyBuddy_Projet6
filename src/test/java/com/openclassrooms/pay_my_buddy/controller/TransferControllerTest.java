package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.dto.TransferBetweenBankAndPayMyBuddyDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
import com.openclassrooms.pay_my_buddy.service.TransferService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransferController.class)
class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransferService transferService;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private UserBankAccountService userBankAccountService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext context;
    private AppUser user1;
    private Role role;
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
        user1.setBalance(100);
        user1.setRoles(roles);
        user1.setPassword(passwordEncoder.encode("12345"));

    }
    @Test
    @WithMockUser
    void transferMoneyToPayMyBuddyUserPostMappingTest() throws Exception {
        // Arrange

        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);

        doNothing().when(userBankAccountService).transferBetweenBankAndPMB(
                "email@gmail.com"
                ,100
                ,"descriptionTest"
                ,OperationType.CREDIT);

        mockMvc.perform(post("/transfer/pmb-bank")
                        .param("amount", String.valueOf(100))
                        .param("description", "descriptionTest")
                        .param("operationType", String.valueOf(OperationType.CREDIT))
                        .param("page", String.valueOf(0))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/transfer?page=0"));

    }

    @Test
    @WithMockUser
    void transferBetweenBankAndPMBGetMappingTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);

        mockMvc.perform(get("/transfer/pmb-bank")
                        .param("amount", "amount")
                        .param("description", "description")
                        .param("operation_type", String.valueOf(OperationType.CREDIT)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("amount", "amount"))
                .andExpect(view().name("transfersBetweenBankAndPMB"));

    }

    @Test
    @WithMockUser
    void showMyTransfersTest() throws Exception {
        // Arrange
        TransferBetweenBankAndPayMyBuddyDTO bankAndPayMyBuddyDTO = new TransferBetweenBankAndPayMyBuddyDTO();
        bankAndPayMyBuddyDTO.setOperationType(OperationType.CREDIT);
        bankAndPayMyBuddyDTO.setDateTransfer(LocalDate.now());
        bankAndPayMyBuddyDTO.setAmount(100);
        bankAndPayMyBuddyDTO.setDescription("Test");
        bankAndPayMyBuddyDTO.setSourceUserBanAccountName("UserBank_LCL");
        bankAndPayMyBuddyDTO.setDestinationPMBUserName("User_Name");
        List<TransferBetweenBankAndPayMyBuddyDTO> betweenBankAndPayMyBuddyDTOS = new ArrayList<>();
        betweenBankAndPayMyBuddyDTOS.add(bankAndPayMyBuddyDTO);

        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(transferService.findAllTransfersByUser(user1)).thenReturn(betweenBankAndPayMyBuddyDTOS);

        mockMvc.perform(get("/myTransfers"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allTransfers",  betweenBankAndPayMyBuddyDTOS))
                .andExpect(view().name("myTransfers"));



    }
}