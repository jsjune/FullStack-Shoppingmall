package com.example.be.config;

import com.example.be.common.dto.ResponseDto;
import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.be.common.exception.ErrorCode.BadCredentialsException;
import static com.example.be.common.exception.ErrorCode.UsernameNotFoundException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String loginException = authException.getClass().getSimpleName();

        if (loginException.equals(UsernameNotFoundException.name())) {
            setResponse(response, UsernameNotFoundException);
        } else if (loginException.equals(BadCredentialsException.name())) {
            setResponse(response, BadCredentialsException);
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(errorCode.getCode(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(fail));

    }

}
