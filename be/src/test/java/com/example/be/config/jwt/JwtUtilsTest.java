package com.example.be.config.jwt;

import com.example.be.common.IntegrationTestSupport;
import com.example.be.common.dto.ResponseDto;
import com.example.be.common.exception.ErrorResponse;
import com.example.be.config.auth.LoginUser;
import com.example.be.user.entity.User;
import com.example.be.user.entity.UserEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static com.example.be.common.exception.ErrorCode.*;
import static com.example.be.config.jwt.JwtProperties.*;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class JwtUtilsTest extends IntegrationTestSupport {

    @Value("${jwt.app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private JwtUtils jwtUtils;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        jwtUtils = new JwtUtils(jwtSecret);
    }

    @DisplayName("request에서 헤더를 추출했을때 토큰이 없을 경우 입니다.")
    @Test
    void parseJwtToken_returnNull() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        String jwtToken = jwtUtils.parseJwtToken(request);

        // then
        assertThat(jwtToken).isNull();
    }

    @DisplayName("HttpServlet request에서 헤더값 추출해서 accessToken을 가져옵니다.")
    @Test
    void parseJwtToken() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String accessToken = "accessToken";
        String token = TOKEN_PREFIX + accessToken;
        request.addHeader(HEADER_STRING, token);

        // when
        String jwtToken = jwtUtils.parseJwtToken(request);

        // then
        assertThat(jwtToken).isEqualTo(accessToken);
    }

    @DisplayName("이메일로 accessToken을 생성합니다.")
    @Test
    void generateAccessTokenFromEmail() {
        // given

        // when
        String accessToken = createToken();

        // then
        assertThat(accessToken).isNotNull();
    }

    @DisplayName("JWT 토큰이 유효합니다.")
    @Test
    void validateJwtToken() throws IOException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String token = createToken();

        // when
        boolean result = jwtUtils.validationJwtToken(token, response);

        // then
        assertThat(result).isTrue();

    }

    @DisplayName("JWT 토큰이 유효하지 않습니다.")
    @Test
    void invalidJwtToken_MalformedJwtException() throws IOException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(INVALID_JWT_TOKEN.getCode(), INVALID_JWT_TOKEN.getMessage());
        String responseDto = objectMapper.writeValueAsString(fail);
        String invalidToken = "invalid_token";

        // when
        boolean result = jwtUtils.validationJwtToken(invalidToken, response);

        // then
        assertThat(result).isFalse();
        assertThat(response.getContentAsString()).isEqualTo(responseDto);
    }

    @DisplayName("JWT 토큰이 만료되었습니다.")
    @Test
    void invalidJwtToken_ExpiredJwtException() throws IOException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(EXPIRED_ACCESS_TOKEN.getCode(), EXPIRED_ACCESS_TOKEN.getMessage());
        String responseDto = objectMapper.writeValueAsString(fail);
        String expiredToken = generateExpiredToken();

        // when
        boolean result = jwtUtils.validationJwtToken(expiredToken, response);

        // then
        assertThat(result).isFalse();
        assertThat(response.getContentAsString()).isEqualTo(responseDto);
    }

    @DisplayName("JWT 토큰 형식이 유효하지 않습니다.")
    @Test
    void invalidJwtToken_UnsupportedJwtException() throws NoSuchAlgorithmException, IOException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseDto<ErrorResponse> fail = ResponseDto.fail(UNSUPPORTED_JWT_TOKEN.getCode(), UNSUPPORTED_JWT_TOKEN.getMessage());
        String responseDto = objectMapper.writeValueAsString(fail);
        String unsupportedAlgorithmToken = generateUnsupportedAlgorithmToken();

        // when
        boolean result = jwtUtils.validationJwtToken(unsupportedAlgorithmToken, response);

        // then
        assertThat(result).isFalse();
        assertThat(response.getContentAsString()).isEqualTo(responseDto);
    }

    @DisplayName("JWT 토큰을 디코딩하여 id와 role을 추출하여 LoginUser 객체를 만든다.")
    @Test
    void verify() {
        // given
        String token = createToken();

        // when
        LoginUser loginUser = jwtUtils.verify(token);

        // then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);

    }

    private String createToken() {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);

        // when
        return jwtUtils.generateAccessTokenFromLoginUser(loginUser);
    }

    private String generateExpiredToken() {
        String email = "test@naver.com";
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis - 3600000;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationTimeMillis))
                .signWith(jwtUtils.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateUnsupportedAlgorithmToken() throws NoSuchAlgorithmException {
        String email = "test@naver.com";
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .compact();
    }

}
