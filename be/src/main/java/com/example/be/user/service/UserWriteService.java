package com.example.be.user.service;

import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.GlobalException;
import com.example.be.config.auth.LoginUser;
import com.example.be.config.jwt.JwtUtils;
import com.example.be.user.dto.CartDto;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.CartResponse;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.entity.Cart;
import com.example.be.user.entity.User;
import com.example.be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.be.user.entity.UserEnum.CUSTOMER;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GlobalException(ErrorCode.EXIST_EMAIL, ErrorCode.EXIST_EMAIL.getMessage());
        }
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(CUSTOMER)
                .profileImg(request.getImage())
                .build();
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(loginUser);
        return new LoginResponse(accessToken, loginUser.getUser());
    }

    public CartResponse addToCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );

        Cart existingCart = user.getCarts()
                .stream()
                .filter(cart -> cart.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
        if (existingCart == null) {
            Cart newCart = new Cart(productId, 1, LocalDateTime.now());
            user.getCarts().add(newCart);
        } else {
            existingCart.addQuantity();
        }

        List<CartDto> cartList = user.getCarts().stream()
                .map(CartDto::new)
                .collect(Collectors.toList());
        return new CartResponse(cartList);
    }

    public void removeCartItem(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );

        List<Cart> carts = user.getCarts();
        carts.removeIf(cart -> cart.getProductId().equals(productId));
        userRepository.save(user);

    }
}
