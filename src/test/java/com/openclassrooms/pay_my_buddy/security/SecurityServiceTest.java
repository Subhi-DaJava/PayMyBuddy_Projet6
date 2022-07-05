package com.openclassrooms.pay_my_buddy.security;

import com.openclassrooms.pay_my_buddy.constant.AuthenticationProvider;
import com.openclassrooms.pay_my_buddy.dto.ConnectionDTO;
import com.openclassrooms.pay_my_buddy.dto.ProfileDTO;
import com.openclassrooms.pay_my_buddy.exception.UserNotExistingException;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.repository.RoleRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityServiceTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private static UserRepository userRepository;
    @MockBean
    private static RoleRepository roleRepository;

    @Autowired
    private SecurityService securityService;

    private AppUser user1;
    private AppUser user2;
    private Set<AppUser> connections;
    private Role role;



    @BeforeEach
    public void init(){
        role = new Role();
        String roleName = "USER";
        role.setRoleName(roleName);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String userEmail = "email@gmail.com";
        user1 = new AppUser();
        user1.setEmail(userEmail);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBalance(0.0);
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
    public void saveAppUserTest(){
        //Given - Arrange
        when(userRepository.save(any(AppUser.class))).thenReturn(user1);
        //When - Action
        AppUser userSaved = securityService.saveUser(user1);
        //Then - Assert
        assertThat(userSaved).isEqualTo(user1);
        assertThat(userSaved.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(userSaved.getPassword()).isEqualTo(user1.getPassword());
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    public void saveAppUserFailureTest(){
        //Given - Arrange
        when(userRepository.save(any(AppUser.class))).thenReturn(null);
        //When - Action
        AppUser userSaved = securityService.saveUser(user1);
        //Then - Assert
        assertThat(userSaved).isNull();

    }

    @Test
    void loadAppUserByUserEmailTest() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(user1);
        // Action
        AppUser findUserByEmail = securityService.loadAppUserByUserEmail("email@gmail.com");
        // Assert

        assertThat(findUserByEmail).isEqualTo(user1);
        verify(userRepository, times(1)).findByEmail(anyString());
    }
    @Test
    void loadAppUserByUserEmailFailureTest() {
        String userEmail = "email@gmail.com";
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        // Action
      /*  AppUser findUserByEmail = securityService.loadAppUserByUserEmail(userEmail);*/
        // Assert
        assertThrows(UserNotExistingException.class, ()-> securityService.loadAppUserByUserEmail(userEmail));

    }

    @Test
    void addRoleToUserTest() {
        // Arrange
        Role newRole = new Role();
        newRole.setRoleName("ADMIN");
        String userEmail = "email@gmail.com";
        // Action
        when(roleRepository.findByRoleName(anyString())).thenReturn(newRole);
        when(userRepository.findByEmail(anyString())).thenReturn(user1);

        securityService.addRoleToUser(userEmail, newRole.getRoleName());

        assertThat(user1.getRoles()).contains(newRole);
    }

    @Test
    void loadRoleByRoleNameTest() {
        // Arrange
        String roleName = "USER";

        // Action
        when(roleRepository.findByRoleName(anyString())).thenReturn(role);

        Role findRoleByRoleName = securityService.loadRoleByRoleName(roleName);

        // Assert

        assertThat(findRoleByRoleName.getRoleName()).isEqualTo(role.getRoleName());

    }
    @Test
    void loadRoleByRoleNameFailureTest() {
        // Arrange
        String roleName = "USER";
        // Action
        when(roleRepository.findByRoleName(anyString())).thenReturn(null);

        // Assert
        assertThatThrownBy(()-> securityService.loadAppUserByUserEmail(roleName));

    }

    @Test
    void findProfileTest() {
        // Arrange
        ProfileDTO profileDTO;
        String userEmail = "email@gmail.com";
        // Action
        when(userRepository.findByEmail(anyString())).thenReturn(user1);

        profileDTO = securityService.findProfile(userEmail);
        // Assert
        assertThat(profileDTO.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(profileDTO.getConnections()).isEqualTo(user1.getConnections());

        verify(userRepository).findByEmail(anyString());

    }
    @Test
    void findProfileFailureTest(){
        // Arrange
        String userEmail = "email@gmail.com";
        // Action
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        // Assert
        assertThatThrownBy(()-> securityService.findProfile(userEmail));
    }

    @Test
    void addAppUserToConnectionTest(){
        // Arrange
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddy@gmail.com";

        // Action
        when(userRepository.findByEmail(userEmail)).thenReturn(user1);
        when(userRepository.findByEmail(buddyEmail)).thenReturn(user2);

        securityService.addAppUserToConnection(userEmail, buddyEmail);

        // Assert
        connections.add(user2);
        assertThat(user1.getConnections()).contains(user2);
        assertThat(user1.getConnections()).isEqualTo(connections);
        verify(userRepository, times(2)).findByEmail(anyString());

    }
    @Test
    void addAppUserToConnectionFailureTest(){
        // Arrange
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddy@gmail.com";

        // Action
        when(userRepository.findByEmail(userEmail)).thenReturn(user1);
        when(userRepository.findByEmail(buddyEmail)).thenReturn(null);

        // Assert

        assertThatThrownBy(() -> securityService.addAppUserToConnection(userEmail, buddyEmail));

    }
    @Test
    void addAppUserToConnectionFailureWithEmailNullTest(){
        // Arrange
        String userEmail = "email@gmail.com";
        String buddyEmail = "";

        // Action
        when(userRepository.findByEmail(userEmail)).thenReturn(user1);

        // Assert
        assertThatThrownBy(() -> securityService.addAppUserToConnection(userEmail, buddyEmail));

    }

    @Test
    void createNewAppUserAfterOAuthLoginSuccessTest() {
        // Arrange
        String name = "googleName";
        String email = "googleEmail@gmail.com";
        AuthenticationProvider provider = AuthenticationProvider.GOOGLE;
        AppUser user = new AppUser();
        user.setFirstName(name);
        user.setLastName("EMPTY_LastName");
        user.setEmail(email);
        user.setAuthProvider(provider);

        // Action
        when(userRepository.save(any(AppUser.class))).thenReturn(user);
        securityService.createNewAppUserAfterOAuthLoginSuccess(email, name, provider);

        // Assert
        assertThat(user.getFirstName()).isEqualTo(name);
        assertThat(user.getRoles()).isEmpty();
        assertThat(user.getPassword()).isNull();

    }

    @Test
    void updateAppUserAfterOAuthLoginSuccessTest() {
        // Arrange
        String name = "googleNameUpdate";
        AuthenticationProvider provider = AuthenticationProvider.LOCAL;

        user1.setFirstName(name);
        user1.setLastName("EMPTY_LastName");
        user1.setAuthProvider(provider);

        // Action
        when(userRepository.findByEmail(anyString())).thenReturn(user1);

        securityService.updateAppUserAfterOAuthLoginSuccess(user1, name, provider);

        // Assert

        assertThat(user1.getFirstName()).isEqualTo(name);
        assertThat(user1.getAuthProvider()).isEqualTo(AuthenticationProvider.LOCAL);

    }

    @Test
    void getConnectionsWithDTOTest() {
        // Arrange
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddy@gmail.com";

        List<ConnectionDTO> connectionDTOS;

        // Action
        connections.add(user2);
        user1.setConnections(connections);

        when(userRepository.findByEmail(userEmail)).thenReturn(user1);
        when(userRepository.findByEmail(buddyEmail)).thenReturn(user2);

        connectionDTOS = securityService.getConnections(user1);

        // Assert
        assertThat(connectionDTOS.get(0).getFirstName()).isEqualTo(user2.getFirstName());
        assertThat(connectionDTOS.get(0).getEmail()).isEqualTo(buddyEmail);

    }

    @Test
    void editAppUserInfoTest() {
        // Arrange
        int userId = 1;
        String userEmail = "emailEdited@gmail.com";
        String firstName = "firstNameEdited";
        String lastName = "lastNameEdited";
        // Action
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.findByEmail(userEmail)).thenReturn(null);

        securityService.editAppUserInfo(userId, firstName, lastName, userEmail);
        // Assert
        assertThat(user1.getFirstName()).isEqualTo(firstName);
        assertThat(user1.getEmail()).isEqualTo(userEmail);

    }

    @Test
    void changePasswordTest() {
        // Arrange
        int userId = 1;
        String password = "54321";
        String rePassword = "54321";
        // Action
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));

        securityService.changePassword(userId, password, rePassword);

        //Assert
        assertThat(passwordEncoder.matches(password, user1.getPassword())).isTrue();
    }

    @Test
    void changePasswordFailureTest() {
        // Arrange
        int userId = 1;
        String password = "54321";
        String rePassword = "654321";
        // Action
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));

        //Assert
        assertThat(passwordEncoder.matches(password, user1.getPassword())).isFalse();
        assertThatThrownBy(() -> securityService.changePassword(userId, password, rePassword));
    }
}