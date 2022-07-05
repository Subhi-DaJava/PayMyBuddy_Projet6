package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserDetailsServiceImplTest {

    @MockBean
    private SecurityService securityService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private PasswordEncoder passwordEncoder;

    private AppUser appUser;
    private Role role;
    @BeforeEach
    public void init(){
        passwordEncoder = new BCryptPasswordEncoder();

        appUser = new AppUser();
        role = new Role();
        String roleName = "USER";
        role.setRoleName(roleName);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String userEmail = "email@gmail.com";
        appUser = new AppUser();
        appUser.setEmail(userEmail);
        appUser.setFirstName("firstName");
        appUser.setLastName("lastName");
        appUser.setBalance(0.0);
        appUser.setRoles(roles);
        appUser.setPassword(passwordEncoder.encode("12345"));
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        String userEmail = "email@gmail.com";

        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(appUser);

        UserDetails user = userDetailsService.loadUserByUsername(userEmail);

        // Assert
        assertThat(user.getUsername()).isEqualTo(userEmail);
        assertThat(user.getPassword()).isEqualTo(appUser.getPassword());

    }
    @Test
    void loadUserByUsernameFailureTest() {
        // Arrange
        String userEmail = "emailNotFound@gmail.com";
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(null);

        // Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(userEmail));

    }
}