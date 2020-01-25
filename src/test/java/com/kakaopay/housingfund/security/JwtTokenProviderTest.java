package com.kakaopay.housingfund.security;

import com.kakaopay.housingfund.user.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider provider;

    @Test
    void createToken() {
        // given
        String email = "test00@test.com";
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);

        // when
        final String token = provider.createToken(email, roles);
        System.out.println("TOKEN = " + token);

        // then
        final String getEmail = provider.getEmail(token);
        assertEquals(email, getEmail);
    }
}