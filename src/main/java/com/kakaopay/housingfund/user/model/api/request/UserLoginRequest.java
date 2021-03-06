package com.kakaopay.housingfund.user.model.api.request;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserLoginRequest {
    @ApiModelProperty(name = "이메일", required = true)
    private final String email;
    @ApiModelProperty(name = "비밀번호", required = true)
    private final String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("password", password)
                .toString();
    }
}
