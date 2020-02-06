package com.kakaopay.housingfund.user.model.api.request;

import com.kakaopay.housingfund.user.model.Role;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class UserJoinRequest {
    @ApiModelProperty(value = "이메일", example = "test00@test.com", required = true)
    private final String email;
    @ApiModelProperty(value = "비밀번호", example = "1234567aA!", required = true)
    private final String password;
    @ApiModelProperty(value = "역할", required = true)
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
