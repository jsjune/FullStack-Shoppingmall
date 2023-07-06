package com.example.be.user.controller;

import com.example.be.common.dto.ResponseDto;
import com.example.be.config.auth.LoginUser;
import com.example.be.user.dto.request.CartRequest;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.CartResponse;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserWriteService userWriteService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest request) {
        userWriteService.signup(request);
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseDto.success(userWriteService.login(request));
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal LoginUser loginUser) {
        log.info(loginUser.getUser().getId() + "_id 로그아웃 요청");
    }

    @PostMapping("/cart")
    public ResponseDto<CartResponse> addToCart(@AuthenticationPrincipal LoginUser loginUser, @RequestBody CartRequest request) {
        Long userId = loginUser.getUser().getId();
        Long productId = request.getProductId();
        return ResponseDto.success(userWriteService.addToCart(userId,productId));
    }

    @PostMapping("/cart/{productId}")
    public void removeCartItem(@AuthenticationPrincipal LoginUser loginUser, @PathVariable("productId") Long productId) {
        Long userId = loginUser.getUser().getId();
        userWriteService.removeCartItem(userId, productId);
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
