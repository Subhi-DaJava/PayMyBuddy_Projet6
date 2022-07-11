package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.security.UserDetailsServiceImpl;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private AppUser admin;
    private Role role;
    private List<Role> roles;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
        passwordEncoder = new BCryptPasswordEncoder();
        role = new Role();
        String roleName = "ADMIN";
        role.setRoleName(roleName);
        roles = new ArrayList<>();
        roles.add(role);

        String adminEmail = "admin@gmail.com";
        admin = new AppUser();
        admin.setEmail(adminEmail);
        admin.setFirstName("firstName");
        admin.setLastName("lastName");
        admin.setRoles(roles);
        admin.setPassword(passwordEncoder.encode("12345"));
    }

    @Test
    @WithMockUser
    void adminDashboardTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(admin);

        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    @WithMockUser
    void addRoleToUserGetMappingTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(admin);

        mockMvc.perform(get("/admin/addRoleToUser")
                        .param("userEmail", "email@gmail.com")
                        .param("roleName", "VISITOR"))
                .andExpect(status().isOk())
                .andExpect(view().name("addRoleToUser"));

    }

    @Test
    @WithMockUser
    void addRoleToUserPostMappingTest() throws Exception {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(admin);
        doNothing().when(securityService).addRoleToUser("email@gmail.com","VISITOR");

        mockMvc.perform(post("/admin/addRoleToUser")
                .param("userEmail", "email@gmail.com")
                .param("roleName", "VISITOR"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/dashboard"));

    }
}