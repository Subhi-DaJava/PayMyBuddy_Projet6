package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.constant.OperationType;
import com.openclassrooms.pay_my_buddy.dto.TransferBetweenBankAndPayMyBuddyDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Role;
import com.openclassrooms.pay_my_buddy.model.Transfer;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.repository.TransferRepository;
import com.openclassrooms.pay_my_buddy.repository.UserBankAccountRepository;
import com.openclassrooms.pay_my_buddy.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @MockBean
    private TransferRepository transferRepository;

    private Transfer transfer;

    private UserBankAccount userBankAccount;
    private AppUser appUser;
    private List<Transfer> transfers;
    @MockBean
    private UserBankAccountRepository userBankAccountRepository;

    @BeforeEach
    public void init(){
        appUser = new AppUser("firstName","lastName",
                "email@Gmail.com","12345", 200);
        appUser.getRoles().add(new Role("USER"));
        userBankAccount = new UserBankAccount("LCL","Paris15",
                "code_iban1575","code_bic7515",appUser);
        transfer = new Transfer();
        String transferDescription = "Transfer description test";
        double amount = 500;

        OperationType operationType = OperationType.CREDIT;

        transfer.setDescription(transferDescription);
        transfer.setAmount(amount);
        transfer.setTransactionDate(LocalDate.now());
        transfer.setOperationType(operationType);
        transfer.setUserBankAccount(userBankAccount);

    }
    @AfterEach
    public void teardown(){
        transfer = null;
        userBankAccount = null;
        appUser = null;
        transfers = new ArrayList<>();
    }

    @Test
    public void saveTransferTest(){
        // Arrange

        // Action
        when(transferRepository.save(any(Transfer.class))).thenReturn(transfer);

        Transfer transferSaved = transferRepository.save(transfer);

        // Assert
        assertThat(transferSaved).isEqualTo(transfer);

    }

    @Test
    public void findAllTransfersByUserTest(){
        // Arrange
        transfers = new ArrayList<>();
        transfers.add(transfer);

        userBankAccount.setTransfers(transfers);

        List<TransferBetweenBankAndPayMyBuddyDTO> allTransfersList;


        // Action
        when(transferRepository.save(transfer)).thenReturn(transfer);
        when(userBankAccountRepository.findByAppUser(appUser)).thenReturn(userBankAccount);
        when(transferRepository.findByUserBankAccountOrderByTransactionDate(userBankAccount))
                .thenReturn(transfers);

         allTransfersList =
                transferService.findAllTransfersByUser(appUser);
        // Assert
        assertThat(allTransfersList).isNotNull();



    }

}