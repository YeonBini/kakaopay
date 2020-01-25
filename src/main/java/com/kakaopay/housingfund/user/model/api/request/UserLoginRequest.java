package com.kakaopay.housingfund.user.model.api.request;

public class UserLoginRequest {
    private final String email;
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

}
