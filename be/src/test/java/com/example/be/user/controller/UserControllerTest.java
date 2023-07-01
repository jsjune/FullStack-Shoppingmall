package com.example.be.user.controller;

import com.example.be.common.ControllerTestSupport;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.entity.User;
import com.example.be.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.be.user.entity.UserEnum.CUSTOMER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 가입에 성공하였습니다.")
    @Test
    void signup() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("abc@naver.com")
                .name("가나다")
                .password("1234")
                .build();

        // when // then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("로그인에 성공하였습니다.")
    @Test
    void login() throws Exception {
        // given
        LoginRequest request = new LoginRequest("abc@naver.com", "1234");
        User user = User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(CUSTOMER)
                .build();
        userRepository.save(user);

        // when // then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
