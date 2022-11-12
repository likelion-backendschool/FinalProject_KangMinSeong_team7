package com.example.eBook.domain.withdraw.service;

import com.example.eBook.domain.cash.entity.CashLog;
import com.example.eBook.domain.cash.service.CashLogService;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.withdraw.dto.AdmWithdrawApplyDto;
import com.example.eBook.domain.withdraw.dto.WithdrawApplyDto;
import com.example.eBook.domain.withdraw.dto.WithdrawApplyForm;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import com.example.eBook.domain.withdraw.exception.CannotApplyWithdrawException;
import com.example.eBook.domain.withdraw.exception.WithdrawNotBelongToMemberException;
import com.example.eBook.domain.withdraw.exception.WithdrawNotFoundException;
import com.example.eBook.domain.withdraw.repository.WithdrawRepository;
import com.example.eBook.global.mapper.WithdrawApplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.eBook.domain.cash.entity.enumuration.CashLogType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;
    private final CashLogService cashLogService;

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

    public void disposeWithdraw(Long id) {
        WithdrawApply withdrawApply = withdrawRepository.findById(id).orElseThrow(
                () -> new WithdrawNotFoundException("해당 출금신청을 찾을 수 없습니다."));

        Member applicant = withdrawApply.getApplicant();
        int withdrawMoney = withdrawApply.getMoney();

        CashLog cashLog = cashLogService.save(applicant, EXCHANGE, withdrawMoney * (-1));
        withdrawApply.disposeWithdraw(cashLog);

        applicant.withdrawRestCash(withdrawMoney);
    }

    @Transactional(readOnly = true)
    public void canApplyWithdraw(String username) {
        Member member = memberService.findByUsername(username);

        List<WithdrawApply> recentWithdrawApply = withdrawRepository.findRecentOneByApplicant(member);

        if (!recentWithdrawApply.isEmpty() && !recentWithdrawApply.get(0).isDone()) {
            throw new CannotApplyWithdrawException("처리되지 않는 출금신청건이 있습니다");
        }
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

    public void delete(Long id, String username) {
        Member applicant = memberService.findByUsername(username);

        WithdrawApply withdrawApply = withdrawRepository.findById(id).orElseThrow(
                () -> new WithdrawNotFoundException("해당 출금신청을 찾을 수 없습니다."));

        if (!withdrawApply.getApplicant().equals(applicant)) {
            throw new WithdrawNotBelongToMemberException("해당 출금신청은 당신의 소유가 아닙니다.");
        }

        withdrawRepository.delete(withdrawApply);
    }
}
