package com.kakaopay.housingfund.user.service;

import com.kakaopay.housingfund.user.exception.EmailSignFailedException;
import com.kakaopay.housingfund.user.exception.UserNotFoundException;
import com.kakaopay.housingfund.user.model.Account;
import com.kakaopay.housingfund.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void join(Account account) {
        checkNotNull(account.getEmail(), "Email can not be null");
        checkNotNull(account.getPassword(), "Password can not be null");
        checkArgument(!userRepository.findByEmail(account.getEmail()).isPresent(),
                "Email already registered");
        account.updatePassword(passwordEncoder.encode(account.getPassword()));
        userRepository.save(account);
    }

    @Transactional
    public Account login(String email, String password) {
        Account account = loadUserByUsername(email);
        account.updateLastLogin();
        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new EmailSignFailedException("Email sign failed");
        }
        return account;
    }

    @Override
    public Account loadUserByUsername(String email) throws UsernameNotFoundException {
        final Account account = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Email does not exists"));
        return account;
    }

}
