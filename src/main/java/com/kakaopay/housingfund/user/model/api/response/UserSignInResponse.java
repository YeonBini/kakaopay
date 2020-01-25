package com.kakaopay.housingfund.user.model.api.response;

public class UserSignInResponse {
    private final String email;
    private final String token;

    public UserSignInResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
