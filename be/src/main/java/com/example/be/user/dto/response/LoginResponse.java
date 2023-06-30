package com.example.be.user.dto.response;

import com.example.be.user.entity.User;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String accessToken;
    private User user;

    public LoginResponse(String accessToken, User loginUser) {
        this.accessToken = accessToken;
        this.user = loginUser;
    }
}
