package com.example.eBook.member.mapper;

import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.global.mapper.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberMapperTest {

    @Test
    @DisplayName("회원가입폼_To_엔티티_Mapper")
    public void signupFormToEntity() {
        SignupForm signupForm = SignupForm.builder()
                .username("test_username")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("test_nickname")
                .email("test@email.com")
                .build();

        Member member = MemberMapper.INSTANCE.signupFormToEntity(signupForm);

        assertThat(member.getUsername()).isEqualTo(signupForm.getUsername());
        assertThat(member.getNickname()).isEqualTo(signupForm.getNickname());
        assertThat(member.getEmail()).isEqualTo(signupForm.getEmail());
        assertThat(member.getAuthLevel()).isEqualTo(3L);

    }
}