package com.example.be.user.controller;

import com.example.be.common.dto.ResponseDto;
import com.example.be.config.auth.LoginUser;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest request) {
        userService.signup(request);
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseDto.success(userService.login(request));
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal LoginUser loginUser) {
        log.info(loginUser.getUser().getId() + "_id 로그아웃 요청");
    }

    @GetMapping("/test")
    public LoginUser test(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser;
    }

    @GetMapping("/admin/test")
    public LoginUser admin(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser;
    }
}
