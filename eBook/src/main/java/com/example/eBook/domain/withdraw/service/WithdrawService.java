package com.example.eBook.domain.withdraw.service;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.withdraw.dto.AdmWithdrawApplyDto;
import com.example.eBook.domain.withdraw.dto.WithdrawApplyDto;
import com.example.eBook.domain.withdraw.dto.WithdrawApplyForm;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import com.example.eBook.domain.withdraw.repository.WithdrawRepository;
import com.example.eBook.global.mapper.WithdrawApplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;

    public void apply(WithdrawApplyForm withdrawApplyForm, String username) {
        Member member = memberService.findByUsername(username);

        WithdrawApply withdrawApply = WithdrawApply.builder()
                .applicant(member)
                .bankName(withdrawApplyForm.getBankName())
                .bankAccountNo(withdrawApplyForm.getBankAccountNo())
                .money(withdrawApplyForm.getMoney())
                .applyDate(LocalDateTime.now())
                .build();

        withdrawRepository.save(withdrawApply);
    }

    @Transactional(readOnly = true)
    public List<WithdrawApplyDto> findAllByMember(String username) {
        Member member = memberService.findByUsername(username);

        return WithdrawApplyMapper.INSTANCE.entitiesToWithdrawApplyDtos(withdrawRepository.findAllByApplicant(member));
    }

    @Transactional(readOnly = true)
    public List<AdmWithdrawApplyDto> findAllAdmWithdrawApplyDto() {
        return WithdrawApplyMapper.INSTANCE.entitiesToAdmWithdrawApplyDtos(withdrawRepository.findAll());
    }
}
