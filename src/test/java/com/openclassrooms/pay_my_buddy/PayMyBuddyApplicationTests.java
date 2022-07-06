package com.openclassrooms.pay_my_buddy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PayMyBuddyApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void passwordEncoderTest() {
        // Arrange
        String password = "12345";

        // Action

        // Assert
        assertThat(new BCryptPasswordEncoder().matches(password, "$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.")).isTrue();

    }
}
