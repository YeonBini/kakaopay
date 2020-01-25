package com.kakaopay.housingfund.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaopay.housingfund.common.Buildable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.regex.Pattern.matches;
import static java.util.stream.Collectors.toList;

@Entity
public class Account implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    protected Account() {}

    protected Account(String email, String password, LocalDateTime createdAt, LocalDateTime lastLogin, List<Role> roles) {
        checkNotNull(email, "Email address must be provided");
        checkArgument(email.length() >= 4 && email.length() < 100,
                "address length must be between 4 and 50 characters.");
        checkArgument(checkAddress(email), "Invalid email address : " + email);
        checkArgument(password.length() >=10 && password.length()<=100,
                "Password should be between 10 and 100 characters");
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.roles = roles;
    }

    private boolean checkAddress(String email) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", email);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public static class Builder implements Buildable<Account> {
        private String email;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime lastLogin;
        private List<Role> roles;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public Builder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        @Override
        public Account build() {
            return new Account(email, password, createdAt, lastLogin, roles);
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getValue())).collect(toList());
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public List<Role> getRole() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(email, account.email) &&
                Objects.equals(password, account.password) &&
                Objects.equals(createdAt, account.createdAt) &&
                Objects.equals(lastLogin, account.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, createdAt, lastLogin);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("email", email)
                .append("password", password)
                .append("createdAt", createdAt)
                .append("lastLogin", lastLogin)
                .toString();
    }
}
