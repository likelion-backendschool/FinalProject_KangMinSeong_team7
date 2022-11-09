package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(member.getRestCash()).isEqualTo(0);
        assertThat(member.getNickname()).isEqualTo(signupForm.getNickname());
        assertThat(member.getEmail()).isEqualTo(signupForm.getEmail());
        assertThat(member.getAuthLevel()).isEqualTo(3L);
    }

    @Test
    @DisplayName("엔티티_To_InfoModifyForm_Mapper")
    public void EntityToInfoModifyForm() {
        Member member = Member.builder()
                .username("test_username")
                .password("1234")
                .email("test@email.com")
                .nickname("test_nickname")
                .authLevel(3L)
                .build();

        InfoModifyForm infoModifyForm = MemberMapper.INSTANCE.EntityToInfoModifyForm(member);

        assertThat(infoModifyForm.getNickname()).isEqualTo(member.getNickname());
        assertThat(infoModifyForm.getEmail()).isEqualTo(member.getEmail());
    }
}