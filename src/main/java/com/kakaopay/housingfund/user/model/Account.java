package com.kakaopay.housingfund.user.model;

import com.kakaopay.housingfund.common.Buildable;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.regex.Pattern.matches;

@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    protected Account() {}

    protected Account(String email, String password, LocalDateTime createdAt, LocalDateTime lastLogin) {
        checkNotNull(email, "Email address must be provided");
        checkArgument(email.length() >= 4 && email.length() < 100,
                "address length must be between 4 and 50 characters.");
        checkArgument(checkAddress(email), "Invalid email address : " + email);
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
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

    public class Builder implements Buildable<Account> {
        private String email;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime lastLogin;

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
        @Override
        public Account build() {
            return new Account(email, password, createdAt, lastLogin);
        }
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
