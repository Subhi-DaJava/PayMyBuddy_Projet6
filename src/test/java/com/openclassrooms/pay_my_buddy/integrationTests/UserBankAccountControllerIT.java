package com.openclassrooms.pay_my_buddy.integrationTests;

import com.openclassrooms.pay_my_buddy.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserBankAccountControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private WebApplicationContext context;

    private final static String APP_USER = "laurentgina@gmail.com";
    private final static String TEST_USER = "test@gmail.com";

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }
    @Test
    void myBankInfoUserBankNullTest() throws Exception {

        mockMvc.perform(get("/myBankAccount")
                        .with(user(TEST_USER).password("12345")))
                .andExpect(content().string(containsString("")));
    }

    @Test
    void addBankAccountGetMappingTest() throws Exception {
        mockMvc.perform(get("/addBankAccount")
                        .with(user(APP_USER).password("12345"))
                        .param("userEmail", APP_USER))
                .andExpect(model().attribute("userEmail", APP_USER))
                .andExpect(view().name("addBankAccount"));
    }
    @Test
    void addBankAccountGetMappingUnauthenticatedTest() throws Exception {
        mockMvc.perform(get("/addBankAccount")
                        .param("userEmail", "APP_USER"))
                .andExpect(model().attribute("userEmail", "APP_USER"))
                .andExpect(view().name("addBankAccount"));
        assertThat(securityService.loadAppUserByUserEmail(APP_USER).getBalance()).isEqualTo(3000);
    }

    @Test
    void addBankAccountToPMBPostMappingTest() throws Exception {
        mockMvc.perform(post("/addBankAccount")
                        .param("userEmail", TEST_USER)
                        .param("bankName","bankName")
                        .param("bankLocation", "Paris15")
                        .param("codeIBAN", "FR7685")
                        .param("codeBIC","BICKLLKYL"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));

        assertThat(securityService.loadAppUserByUserEmail(TEST_USER)
                .getUserBankAccount().getBankName()).isEqualTo("bankName");
    }

    @Test
    void addBankAccountToPMBPostMappingUserEmailNullTest() throws Exception {
        mockMvc.perform(post("/addBankAccount")
                        .param("userEmail", "")
                        .param("bankName","bankName")
                        .param("bankLocation", "Paris15")
                        .param("codeIBAN", "FR7685")
                        .param("codeBIC","BICKLLKYL"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBankAccount"));

        assertThat(securityService.loadAppUserByUserEmail(TEST_USER)
                .getFirstName()).isEqualTo("firstName");
    }
}