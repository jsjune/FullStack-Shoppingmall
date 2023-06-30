package com.example.be.config;

import com.example.be.common.dto.ResponseDto;
import com.example.be.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.ServletException;
import java.io.IOException;

import static com.example.be.common.exception.ErrorCode.INVALID_JWT_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class CustomAuthenticationEntryPointTest {

    @DisplayName("아이디가 존재하지 않을 때 UsernameNotFoundException 예외가 터진다.")
    @Test
    void UsernameNotFoundException() throws ServletException, IOException {
        // given
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(
                ResponseDto.fail(
                        ErrorCode.UsernameNotFoundException.getCode(),
                        ErrorCode.UsernameNotFoundException.getMessage()
                )
        );

        // when
        entryPoint.commence(request, response, new UsernameNotFoundException("User not found"));

        // then
        assertThat(response.getStatus()).isEqualTo(INVALID_JWT_TOKEN.getHttpStatus().value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

    }

    @DisplayName("비밀번호가 틀릴 때 BadCredentialsException 예외가 터진다.")
    @Test
    void BadCredentialsException() throws ServletException, IOException {
        // given
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(
                ResponseDto.fail(
                        ErrorCode.BadCredentialsException.getCode(),
                        ErrorCode.BadCredentialsException.getMessage()
                )
        );

        // when
        entryPoint.commence(request, response, new BadCredentialsException("Invalid credentials"));

        // then
        assertThat(response.getStatus()).isEqualTo(INVALID_JWT_TOKEN.getHttpStatus().value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

    }
}
