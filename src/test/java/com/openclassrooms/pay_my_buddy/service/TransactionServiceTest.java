package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.FeeApplication;
import com.openclassrooms.pay_my_buddy.dto.Payment;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionServiceTest {
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;
    private Role role;
    private AppUser user1;
    private AppUser user2;
    private Set<AppUser> connections;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

        String buddyEmail = "buddy@gmail.com";
        user2 = new AppUser();
        user2.setEmail(buddyEmail);
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setBalance(100);
        user2.setRoles(roles);
        user2.setPassword(passwordEncoder.encode("12345"));

        connections = new HashSet<>();
        connections.add(user2);
        user1.setConnections(connections);
    }

    @AfterEach
    public void teardown(){
        user1 = null;
        user2 = null;
        role = null;
        connections = new HashSet<>();
    }

    @Test
    void sendMoneyToBuddyTest() {
        // Arrange
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddyEmail@gmail.com";
        double amount = 50;
        String description = "Test Description";

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTotalFeePayed(amount * FeeApplication.FEE);

        // Action
        when(userRepository.findByEmail(userEmail)).thenReturn(user1);
        when(userRepository.findByEmail(buddyEmail)).thenReturn(user2);
        when(userRepository.save(user1)).thenReturn(user1);

        when(userRepository.save(user2)).thenReturn(user2);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        transactionService.sendMoneyToBuddy(userEmail, buddyEmail, amount, description);

        // Assert
        assertThat(user1.getBalance()).isEqualTo(449.75);
        assertThat(user2.getBalance()).isEqualTo(150.0);
        assertThat(transaction.getDescription()).isEqualTo(description);
        assertThat(transaction.getTotalFeePayed()).isEqualTo(0.25);
    }
    @Test
    void sendMoneyToBuddyFailureWithAmountEqualsZeroTest() {
        // Arrange
        String userEmail = "email@gmail.com";
        String buddyEmail = "buddyEmail@gmail.com";
        double amount = 0;
        String description = "Test Description";

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTotalFeePayed(amount * FeeApplication.FEE);

        // Action
        when(userRepository.findByEmail(userEmail)).thenReturn(user1);
        when(userRepository.findByEmail(buddyEmail)).thenReturn(user2);
        when(userRepository.save(user1)).thenReturn(user1);

        when(userRepository.save(user2)).thenReturn(user2);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Assert
        assertThatThrownBy(() -> transactionService.sendMoneyToBuddy(userEmail,buddyEmail ,amount,description));
    }

    @Test
    void findTransactionsBySourceTest() {
        // Arrange
        Transaction transaction = new Transaction();
        double amount = 50;
        String description = "Test Description";
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTotalFeePayed(0.005);
        transaction.setTarget(user2);
        List<Transaction> myTransactions = new ArrayList<>();
        myTransactions.add(transaction);

        // Action
        when(transactionRepository.findBySource(any(AppUser.class))).thenReturn(myTransactions);

        List<Payment> myPayments = transactionService.findTransactionsBySource(user1);

        // Assert
        double amount_transaction = myPayments.get(0).getAmont();
        String description_transaction = myPayments.get(0).getDescription();

        assertThat(amount_transaction).isEqualTo(amount);
        assertThat(description_transaction).isEqualTo(description);

    }
}