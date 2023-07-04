package com.example.be.user.repository;

import com.example.be.common.IntegrationTestSupport;
import com.example.be.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends IntegrationTestSupport{
    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 가입할 때 이메일이 존재하는지 확인하기")
    @Test
    void existsByEmail() {
        // given
        String email = "abc@naver.com";
        User user = User.builder()
                .email(email)
                .build();
        userRepository.save(user);

        // when
        boolean result = userRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }
}
