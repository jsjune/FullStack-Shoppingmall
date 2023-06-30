package com.example.be.config.jwt;

import com.example.be.common.dto.ResponseDto;
import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.ErrorResponse;
import com.example.be.config.auth.LoginUser;
import com.example.be.user.entity.User;
import com.example.be.user.entity.UserEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

import static com.example.be.common.exception.ErrorCode.*;
import static com.example.be.config.jwt.JwtProperties.*;

@Slf4j
@Component
@Getter
public class JwtUtils {
    private final Key key;

    //The specified key byte array is 248 bits which is not secure enough for any JWT HMAC-SHA algorithm.
    // The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a size >= 256 bits (the key size must be greater than or equal to the hash output size).
    // Consider using the io.jsonwebtoken.security.Keys#secretKeyFor(SignatureAlgorithm) method to create a key guaranteed to be secure enough for your preferred HMAC-SHA algorithm.
    public JwtUtils(@Value("${jwt.app.jwtSecret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String parseJwtToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }

    public boolean validationJwtToken(String token, HttpServletResponse response) throws IOException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            setResponse(response, INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            setResponse(response, EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
            setResponse(response, UNSUPPORTED_JWT_TOKEN);
        }
        return false;
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(errorCode.getCode(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(fail));
    }


    public String generateAccessTokenFromLoginUser(LoginUser loginUser) {
        return Jwts.builder()
                .claim("id",loginUser.getUser().getId())
                .claim("role",loginUser.getUser().getRole()+"")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public LoginUser verify(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long id = claims.get("id", Long.class);
        String role = claims.get("role", String.class);
        User user = User.builder()
                .id(id)
                .role(UserEnum.valueOf(role))
                .build();
        return new LoginUser(user);
    }
}
