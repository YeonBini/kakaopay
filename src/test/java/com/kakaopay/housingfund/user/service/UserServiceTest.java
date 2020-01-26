package com.kakaopay.housingfund.user.service;

import com.kakaopay.housingfund.exception.EmailSignFailedException;
import com.kakaopay.housingfund.user.model.Account;
import com.kakaopay.housingfund.user.model.Role;
import com.kakaopay.housingfund.user.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Account account;

    @Test
    @Order(1)
    @Rollback(false)
    void join() {
        // given
        account = new Account.Builder()
                .email("test@test.com")
                .password("1234567aA!")
                .createdAt(LocalDateTime.now())
                .roles(Arrays.asList(Role.USER))
                .build();

        // when
        userService.join(account);

        // then
        final Account findAccount = userRepository.findByEmail(this.account.getEmail()).get();
        assertEquals(account.getEmail(), findAccount.getEmail());
    }

    @Test
    @Order(2)
    void login() {
        // when
        String email = "test@test.com";
        String password = "1234567aA!";
        final Account login = userService.login(email, password);

        // then
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        final String now = LocalDateTime.now().format(formatter);
        final String lastLoginTime = login.getLastLogin().format(formatter);
        assertEquals(now, lastLoginTime);
    }

    @Test
    @Order(3)
    void login_failed() {
        // when
        String email = "test@test.com";
        String password = "1234567";

        // then
        assertThrows(EmailSignFailedException.class, () -> userService.login(email, password));
    }

    @Test
    @Order(4)
    void loadUserByUsername() {
        // when
        String email = "test@test.com";
        final Account account = userService.loadUserByUsername(email);

        // then
        assertEquals(email, account.getEmail());
    }
}