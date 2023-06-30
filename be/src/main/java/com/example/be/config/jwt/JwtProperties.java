package com.example.be.config.jwt;

public interface JwtProperties {
    long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000L; // 30분
//    long ACCESS_TOKEN_EXPIRATION_TIME =1000L; // 1초
    long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    long REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS = REFRESH_TOKEN_EXPIRE_TIME / 1000; // 7일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String EXCEPTION = "exception";
}
