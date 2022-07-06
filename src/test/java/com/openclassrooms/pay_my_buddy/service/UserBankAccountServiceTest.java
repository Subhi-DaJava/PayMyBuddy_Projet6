package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.repository.UserBankAccountRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class UserBankAccountServiceTest {
    @Autowired
    private UserBankAccountService userBankAccountService;
    @MockBean
    private UserBankAccountRepository userBankAccountRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SecurityService securityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AppUser user1;
    private Role role;
    private UserBankAccount userBankAccount;

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
        user1.setBalance(500);
        user1.setRoles(roles);
        user1.setPassword(passwordEncoder.encode("12345"));

        userBankAccount = new UserBankAccount();
        userBankAccount.setBankName("bankName");
        userBankAccount.setBankLocation("bankLocation");
        userBankAccount.setCodeIBAN("codeIban");
        userBankAccount.setCodeBIC("codeBic");

    }
    @AfterEach
    public void teardown(){
        user1 = null;
        role = null;
        userBankAccount = null;

    }
    @Test
    void addBankAccountToPayMyBuddyTest() {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountRepository.save(any(UserBankAccount.class))).thenReturn(userBankAccount);
        when(userBankAccountRepository.findByAppUser(any(AppUser.class))).thenReturn(userBankAccount);

        userBankAccountService.addBankAccountToPayMyBuddy(
                "email@gmail.com",
                "bankName",
                "bankLocation",
                "codeIban",
                "codeBic");

        // Assert
        assertThat(user1.getUserBankAccount().getBankName()).isEqualTo("bankName");
        assertThat(user1.getUserBankAccount().getCodeBIC()).isEqualTo("codeBic");
    }
    @Test
    void addBankAccountToPayMyBuddyFailureTest() {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountRepository.findByCodeIBAN(anyString())).thenReturn(userBankAccount);
        when(userBankAccountRepository.findByAppUser(any(AppUser.class))).thenReturn(userBankAccount);

        // Assert
        assertThatThrownBy(()-> userBankAccountService.addBankAccountToPayMyBuddy("email@gmail.com",
                "bankName",
                "bankLocation",
                "codeIban",
                "codeBic"));
    }

    @Test
    void findUserBankAccountByIdTest() {
        // Arrange
        int ubaId = 1;
        when(userBankAccountRepository.findById(anyInt())).thenReturn(Optional.of(userBankAccount));
        // Action
        UserBankAccount findUserBankAccountById = userBankAccountService.findUserBankAccountById(ubaId);

        // Assert
        assertThat(findUserBankAccountById.getBankName()).isEqualTo("bankName");
        assertThat(findUserBankAccountById.getCodeIBAN()).isEqualTo("codeIban");

    }

    @Test
    void findUserBankAccountByIdFailureTest() {
        // Arrange
        int ubaId = 1;

        when(userBankAccountRepository.findById(anyInt())).thenReturn(null);
        // Action

        // Assert
        assertThatThrownBy(()->userBankAccountService.findUserBankAccountById(ubaId));

    }


    @Test
    void transferBetweenBankAndPMBTest() {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountRepository.findByAppUser(any(AppUser.class))).thenReturn(userBankAccount);
        when(userRepository.save(any(AppUser.class))).thenReturn(user1);

        userBankAccountService.transferBetweenBankAndPMB(
                "eamil@gmail.com",
                50,
                "description",
                OperationType.CREDIT);
        // Assert
        assertThat(user1.getBalance()).isEqualTo(550.0);
    }
    @Test
    void transferBetweenPMBAndBankTest() {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountRepository.findByAppUser(any(AppUser.class))).thenReturn(userBankAccount);
        when(userRepository.save(any(AppUser.class))).thenReturn(user1);

        userBankAccountService.transferBetweenBankAndPMB(
                "eamil@gmail.com",
                50,
                "description",
                OperationType.DEBIT);
        // Assert
        assertThat(user1.getBalance()).isEqualTo(450.0);
    }

    @Test
    void transferBetweenPMBAndBankFailureTest() {
        // Arrange
        // Action
        when(securityService.loadAppUserByUserEmail(anyString())).thenReturn(user1);
        when(userBankAccountRepository.findByAppUser(any(AppUser.class))).thenReturn(userBankAccount);
        when(userRepository.save(any(AppUser.class))).thenReturn(user1);


        // Assert
        assertThatThrownBy(() -> userBankAccountService.transferBetweenBankAndPMB(
                "eamil@gmail.com",
                500,
                "description",
                OperationType.DEBIT));
    }

    @Test
    void bankAccountInfoTest() {
        // Arrange
        userBankAccount.setAppUser(user1);
        user1.setUserBankAccount(userBankAccount);
        // Action

        when(userBankAccountRepository.save(any(UserBankAccount.class))).thenReturn(userBankAccount);
        when(userBankAccountRepository.findByAppUser(any(AppUser.class))).thenReturn(userBankAccount);
        userBankAccountService.bankAccountInfo(user1);

        // Assert
        assertThat(userBankAccount.getAppUser().getUserBankAccount().getBankName()).isEqualTo("bankName");
        assertThat(userBankAccount.getAppUser().getUserBankAccount().getCodeIBAN()).isEqualTo("codeIban");
        assertThat(userBankAccount.getAppUser().getUserBankAccount().getBankLocation()).isEqualTo("bankLocation");
    }

    @Test
    void findByCodeIBANTest(){
        // Arrange

        // Action
        when(userBankAccountRepository.findByCodeIBAN(anyString())).thenReturn(userBankAccount);
        userBankAccountService.findByCodeIBAN("codeIban");
        // Assert
        assertThat(userBankAccount.getBankName()).isEqualTo("bankName");
    }

}