package com.example.be.user.service;

import com.example.be.common.exception.GlobalException;
import com.example.be.config.auth.LoginUser;
import com.example.be.config.jwt.JwtUtils;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.entity.User;
import com.example.be.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.be.common.exception.ErrorCode.EXIST_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserWriteServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserWriteService userWriteService;

    @DisplayName("이메일이 이미 존재하여 회원 가입을 실패합니다.")
    @Test
    void signupUserFail() {
        // given
        String email = "abc@naver.com";
        SignupRequest request = SignupRequest.builder()
                .email(email)
                .build();

        // when
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // then
        assertThatThrownBy(() -> userWriteService.signup(request))
                .isInstanceOf(GlobalException.class)
                .satisfies(ex -> {
                    assertThat(((GlobalException) ex).getErrorCode()).isEqualTo(EXIST_EMAIL);
                });
    }

    @DisplayName("회원 가입을 합니다.")
    @Test
    void test() {
        // given
        String email = "abc@naver.com";
        String password = "1234";
        String username = "가나다";
        SignupRequest request = SignupRequest.builder()
                .email(email)
                .name(username)
                .password(password)
                .build();

        // when
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        userWriteService.signup(request);

        // then
        verify(userRepository).existsByEmail(request.getEmail());
        verify(bCryptPasswordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @DisplayName("로그인을 합니다.")
    @Test
    void login() {
        // given
        Authentication authentication = mock(Authentication.class);
        LoginUser loginUser = mock(LoginUser.class);
        User user = mock(User.class);

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(loginUser);
        when(jwtUtils.generateAccessTokenFromLoginUser(loginUser)).thenReturn("accessToken");
        when(loginUser.getUser()).thenReturn(user);

        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        LoginResponse loginResponse = userWriteService.login(loginRequest);

        // then
        assertThat(loginResponse.getAccessToken()).isEqualTo("accessToken");
        assertThat(loginResponse.getUser()).isEqualTo(user);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).getPrincipal();
        verify(jwtUtils).generateAccessTokenFromLoginUser(loginUser);
    }
}
