package com.kakaopay.housingfund.security;

import com.kakaopay.housingfund.user.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider provider;

    @Test
    void createToken() {
        // given
        String email = "test00@test.com";
        Role role = Role.USER;

        // when
        final String token = provider.createToken(email, role);
        System.out.println("TOKEN = " + token);

        // then
        final String getEmail = provider.getEmail(token);
        assertEquals(email, getEmail);
    }
}