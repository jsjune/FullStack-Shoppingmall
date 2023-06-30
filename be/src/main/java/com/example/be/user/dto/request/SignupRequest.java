package com.example.be.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String image;

    @Builder
    public SignupRequest(String email, String password, String name, String image) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
    }
}
