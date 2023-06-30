package com.example.be.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "USER_01", "존재하는 이메일 입니다."),
    UsernameNotFoundException(HttpStatus.UNAUTHORIZED, "USER_02", "계정이 존재하지 않습니다."),
    BadCredentialsException(HttpStatus.UNAUTHORIZED, "USER_03", "비밀번호가 불일치 합니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_01", "JWT 토큰이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_02", "토큰시간이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_03", "지원하지 않는 토큰입니다."),
    NOT_IMAGE_FILE(HttpStatus.BAD_REQUEST, "PRODUCT_01", "이미지 파일만 업로드 가능합니다!");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
