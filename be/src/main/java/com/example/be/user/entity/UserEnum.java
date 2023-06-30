package com.example.be.user.entity;

import lombok.Getter;

@Getter
public enum UserEnum {
    ADMIN("관리자"), CUSTOMER("고객");

    private String value;

    UserEnum(String value) {
        this.value = value;
    }
}
