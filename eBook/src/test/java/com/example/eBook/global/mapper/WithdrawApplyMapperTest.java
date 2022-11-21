package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.withdraw.dto.AdmWithdrawApplyDto;
import com.example.eBook.domain.withdraw.dto.WithdrawApplyDto;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class WithdrawApplyMapperTest {

    @Test
    @DisplayName(value = "entity_To_WithdrawApplyDto_Mapper")
    void entityToWithdrawApplyDto() {

        WithdrawApply withdrawApply = WithdrawApply.builder()
                .id(1L)
                .applicant(new Member())
                .bankName("신한은행")
                .bankAccountNo("111-111-111")
                .money(1000)
                .applyDate(LocalDateTime.now())
                .build();

        WithdrawApplyDto withdrawApplyDto = WithdrawApplyMapper.INSTANCE.entityToWithdrawApplyDto(withdrawApply);

        assertThat(withdrawApplyDto.getId()).isEqualTo(withdrawApply.getId());
        assertThat(withdrawApplyDto.getBankName()).isEqualTo(withdrawApply.getBankName());
        assertThat(withdrawApplyDto.getBankAccountNo()).isEqualTo(withdrawApply.getBankAccountNo());
        assertThat(withdrawApplyDto.getMoney()).isEqualTo(withdrawApply.getMoney());
        assertThat(withdrawApplyDto.getWithdrawDate()).isEqualTo(withdrawApply.getWithdrawDate());
        assertThat(withdrawApplyDto.getApplyDate()).isEqualTo(withdrawApply.getApplyDate());
    }

    @Test
    @DisplayName(value = "entity_To_AdmWithdrawApplyDto_Mapper")
    void entityToAdmWithdrawApplyDto() {

        WithdrawApply withdrawApply = WithdrawApply.builder()
                .id(1L)
                .applicant(Member.builder().nickname("test_nickname").build())
                .bankName("신한은행")
                .bankAccountNo("111-111-111")
                .money(1000)
                .applyDate(LocalDateTime.now())
                .build();

        AdmWithdrawApplyDto admWithdrawApplyDto =
                WithdrawApplyMapper.INSTANCE.entityToAdmWithdrawApplyDto(withdrawApply);

        assertThat(admWithdrawApplyDto.getId()).isEqualTo(withdrawApply.getId());
        assertThat(admWithdrawApplyDto.getApplicantName()).isEqualTo(withdrawApply.getApplicant().getNickname());
        assertThat(admWithdrawApplyDto.getBankName()).isEqualTo(withdrawApply.getBankName());
        assertThat(admWithdrawApplyDto.getBankAccountNo()).isEqualTo(withdrawApply.getBankAccountNo());
        assertThat(admWithdrawApplyDto.getMoney()).isEqualTo(withdrawApply.getMoney());
        assertThat(admWithdrawApplyDto.getApplyDate()).isEqualTo(withdrawApply.getApplyDate());
        assertThat(admWithdrawApplyDto.isCanApply()).isEqualTo(!withdrawApply.isDone());
    }
}