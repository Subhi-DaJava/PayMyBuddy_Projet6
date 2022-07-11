package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.constant.FeeApplication;
import com.openclassrooms.pay_my_buddy.dto.ConnectionDTO;
import com.openclassrooms.pay_my_buddy.dto.ProfileDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private SecurityService securityService;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    private AppUser user1;
    private AppUser user2;
    private Role role;
    private Set<AppUser> connections;
    private List<Role> roles;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
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
    @AfterEach
    public void teardown(){
        user1 = null;
        user2 = null;
        role = null;
        connections = new HashSet<>();
    }

    @Test
    @WithMockUser
    void addBuddyPostMappingTest() throws Exception {
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddy@gmail.com";
        connections.add(user2);
        user1.getConnections().add(user2);
        String url = "/addBuddy";
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user2);
        doNothing().when(securityService).addAppUserToConnection(userEmail, buddyEmail);

        mockMvc.perform(post(url)
                .param("buddyEmail", buddyEmail)
                        .with(csrf()))
                .andExpect(view().name("redirect:/transfer?page=0"));

    }

    @Test
    @WithMockUser
    void addConnectionGetMappingTest() throws Exception {
        connections.add(user2);
        user1.getConnections().add(user2);
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddy@gmail.com";
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user2);
        doNothing().when(securityService).addAppUserToConnection(userEmail, buddyEmail);

        mockMvc.perform(get("/addConnection"))
                .andExpect(status().isOk())
                .andExpect(view().name("formAddConnection"));
    }

    @Test
    @WithMockUser
    void showPageTransferGetMappingTest() throws Exception {
        Page<Transaction> page = getPage(2, 6); // 2
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(transactionRepository.findAllBySource(Pageable.ofSize(3), user1)).thenReturn(page);

        mockMvc.perform(get("/transfer")
                .param("amount", "10")
                .param("description", "description")
                .param("buddyEmail", "buddyEmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"));
    }

    @Test
    @WithMockUser
    void myProfileTest() throws Exception {
        // Arrange
        Set<String> roleList = new HashSet<>();
        roleList.add("USER");
        ProfileDTO profile = new ProfileDTO();
        profile.setBalance(100);
        profile.setEmail("email@gmail.com");
        profile.setFirstName("firstName");
        profile.setLastName("lastName");
        profile.setRoles(roleList);
        profile.setConnections(new HashSet<>());
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(securityService.findProfile(anyString())).thenReturn(profile);
        when(userRepository.findByEmail(anyString())).thenReturn(user1);

        mockMvc.perform(get("/myProfile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    @WithMockUser
    void myConnectionsTest() throws Exception {
        // Arrange
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setFirstName("buddyFN");
        connectionDTO.setLastName("buddyLN");
        connectionDTO.setEmail("buddy@gmail.com");
        List<ConnectionDTO> connectionDTOList = new ArrayList<>();
        connectionDTOList.add(connectionDTO);

        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(securityService.getConnections(any(AppUser.class))).thenReturn(connectionDTOList);

        mockMvc.perform(get("/myConnections"))
                .andExpect(status().isOk())
                .andExpect(view().name("myConnections"));


    }

    @Test
    @WithMockUser
    void editUserGetMappingTest() throws Exception {
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);

        mockMvc.perform(get("/editUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("editUser"));
    }

    @Test
    @WithMockUser
    void editProfilePostMappingTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        doNothing().when(securityService).editAppUserInfo(0,"firstNameUpdated", "lastNameUpdated", "emailEE@gmail.com");

        mockMvc.perform(post("/editUser")
                        .param("firstName", "firstNameUpdated")
                        .param("lastName","lastNameUpdated")
                        .param("email","emailEE@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Test
    @WithMockUser
    void changePasswordGetMappingTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        mockMvc.perform(get("/changePassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePassword"));
    }

    @Test
    @WithMockUser
    void updatePasswordPostMappingTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        doNothing().when(securityService).changePassword(0,"54321","54321");

        mockMvc.perform(post("/changePassword")
                .param("password","54321")
                .param("rePassword","54321"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));


    }

    /*
        implementation of Page
     */
    private Page<Transaction> getPage(int pageSize, int totalRecords)
    {
        return  new PageImpl<Transaction>(
                List.of(new Transaction(100, "Transfer 1", 10, user2),
                        new Transaction(500, "Transfer 2", 50, user2),
                        new Transaction(100, "Transfer 3", 10, user2)),
                Pageable.ofSize(pageSize),
                totalRecords);
    }
}