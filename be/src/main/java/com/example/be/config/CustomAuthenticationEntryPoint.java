package com.example.be.config;

import com.example.be.common.dto.ResponseDto;
import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String loginException = authException.getClass().getSimpleName();
        if (loginException.equals("UsernameNotFoundException")) {
            setResponse(response, ErrorCode.NOT_FOUND_EMAIL, authException);
        } else if (loginException.equals("BadCredentialsException")) {
            setResponse(response, ErrorCode.INVALID_PASSWORD, authException);
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode, Exception e) throws IOException {
        log.error(errorCode.getMessage(), e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(errorCode.getCode(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(fail));
    }

}
