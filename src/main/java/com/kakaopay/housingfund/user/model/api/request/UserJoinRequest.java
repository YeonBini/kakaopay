package com.kakaopay.housingfund.user.model.api.request;

import com.kakaopay.housingfund.user.model.Role;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class UserJoinRequest {
    private final String email;
    private final String password;
    private final List<Role> roles;

    public UserJoinRequest(String email, String password, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("password", password)
                .append("roles", roles)
                .toString();
    }
}
