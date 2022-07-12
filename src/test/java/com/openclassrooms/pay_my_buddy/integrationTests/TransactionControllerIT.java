package com.openclassrooms.pay_my_buddy.integrationTests;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.security.SecurityService;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WebApplicationContext context;

    private final static String APP_USER = "laurentgina@gmail.com";
    private final static String TEST_USER = "sophiefoncek@gmail.com";

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    @Transactional
    void sendMoneyToBuddyPostMappingTest() throws Exception {

        AppUser buddy = securityService.loadAppUserByUserEmail(TEST_USER);
        transactionService.sendMoneyToBuddy(APP_USER, TEST_USER, 50.0, "test");
        assertThat(buddy.getBalance()).isEqualTo(4050.0);
    }

}