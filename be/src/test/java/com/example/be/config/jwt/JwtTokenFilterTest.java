package com.example.be.config.jwt;

import com.example.be.config.auth.LoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @DisplayName("토큰이 유효하여 필터를 통과한다.")
    @Test
    void validToken_filterSuccess() throws ServletException, IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        LoginUser loginUser = mock(LoginUser.class);

        // when
        String validToken = "validToken";
        when(jwtUtils.parseJwtToken(request)).thenReturn(validToken);
        when(jwtUtils.validationJwtToken(validToken, response)).thenReturn(true);
        when(jwtUtils.verify(validToken)).thenReturn(loginUser);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        Authentication resultAuthentication = SecurityContextHolder.getContext().getAuthentication();

        // then
        assertThat(resultAuthentication.getPrincipal()).isEqualTo(loginUser);

        verify(filterChain).doFilter(request, response);

    }
}
