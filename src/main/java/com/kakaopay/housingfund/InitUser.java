package com.kakaopay.housingfund;

import com.kakaopay.housingfund.user.model.Account;
import com.kakaopay.housingfund.user.model.Role;
import com.kakaopay.housingfund.user.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static java.time.LocalDateTime.now;

@Profile("dev")
@Component
public class InitUser {

    private final UserService userService;

    public InitUser(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void initUser() {
        Account account = new Account.Builder()
                .email("test00@test.com")
                .password("1234567aA!")
                .roles(Arrays.asList(Role.USER))
                .createdAt(now())
                .build();
        userService.join(account);
    }
}
