package com.example.be.docs.user;

import com.example.be.common.exception.GlobalException;
import com.example.be.docs.RestDocsSupport;
import com.example.be.user.controller.UserController;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.entity.User;
import com.example.be.user.service.UserWriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.example.be.common.exception.ErrorCode.INVALID_JWT_TOKEN;
import static com.example.be.user.entity.UserEnum.CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerDocsTest extends RestDocsSupport {

    private final UserWriteService userWriteService = mock(UserWriteService.class);

    @Override
    protected Object initController() {
        return new UserController(userWriteService);
    }

    @DisplayName("회원 가입 API")
    @Test
    void signup() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("abc@naver.com")
                .name("가나다")
                .password("1234")
                .image("이미지")
                .build();

        // when // then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-signup",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일 형식이어야 합니다."),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호는 6자 이상이어야 합니다."),
                                fieldWithPath("image").type(JsonFieldType.STRING)
                                        .description("프로필 이미지")
                        )
                ));
    }

    @DisplayName("로그인 성공 API")
    @Test
    void login_success() throws Exception {
        // given
        LoginRequest request = new LoginRequest("abc@naver.com", "1234");
        LoginResponse mockResponse = new LoginResponse("accessToken",
                User.builder()
                        .id(1L)
                        .email("abc@naver.com")
                        .name("test")
                        .password("password")
                        .role(CUSTOMER)
                        .profileImg("profile_image")
                        .build()
        );
        given(userWriteService.login(any(LoginRequest.class))).willReturn(mockResponse);

        // when // then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일 형식이어야 합니다."),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호는 6자 이상이어야 합니다.")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("어세스 토큰"),
                                fieldWithPath("data.user.id").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("data.user.email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data.user.name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("data.user.password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("data.user.role").type(JsonFieldType.STRING)
                                        .description("권한"),
                                fieldWithPath("data.user.profileImg").type(JsonFieldType.STRING)
                                        .description("프로필 이미지"),
                                fieldWithPath("error").optional()
                                        .description("에러")
                        )
                ));
    }

    @DisplayName("토큰 만료 API")
    @Test
    void login_fail() throws Exception {
        // given
        LoginRequest request = new LoginRequest("abc@naver.com", "1234");

        given(userWriteService.login(any(LoginRequest.class))).willThrow(new GlobalException(INVALID_JWT_TOKEN));

        // when // then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is(401))
                .andDo(document("user-login-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일 형식이어야 합니다."),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호는 6자 이상이어야 합니다.")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부"),
                                fieldWithPath("data").type(JsonFieldType.STRING).optional()
                                        .description("데이터"),
                                fieldWithPath("error.code").type(JsonFieldType.STRING)
                                        .description("에러 코드"),
                                fieldWithPath("error.message").type(JsonFieldType.STRING)
                                        .description("에러 메시지")
                        )
                ));
    }

}
