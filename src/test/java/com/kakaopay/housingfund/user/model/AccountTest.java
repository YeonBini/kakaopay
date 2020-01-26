package com.kakaopay.housingfund.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account;

    @BeforeEach
    void setUp() {
        // given
        account = new Account.Builder()
                .email("test@test.com")
                .password("1234567aA!")
                .createdAt(LocalDateTime.now())
                .roles(Arrays.asList(Role.USER))
                .build();
    }

    @Test
    void updatePassword() {
        // when
        final String password = "1234567890";
        account.updatePassword(password);

        // then
        assertEquals(password, account.getPassword());
    }

    @Test
    void updatePassword_exception() {
        // when
        final String password = "1234";

        // then
        assertThrows(IllegalArgumentException.class, () -> account.updatePassword(password));
    }

    @Test
    void updateLastLogin() {
        // when
        account.updateLastLogin();

        // then
        assertNotNull(account.getLastLogin());
    }
}