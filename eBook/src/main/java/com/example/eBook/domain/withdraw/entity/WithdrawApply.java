package com.example.eBook.domain.withdraw.entity;

import com.example.eBook.domain.base.BaseTimeEntity;
import com.example.eBook.domain.cash.entity.CashLog;
import com.example.eBook.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawApply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdraw_apply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member applicant;

    private String bankName;
    private String bankAccountNo;
    private int money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_log_id")
    private CashLog withdrawCashLog;

    private LocalDateTime applyDate;
    private LocalDateTime withdrawDate;

    public boolean isDone() {
        return this.withdrawDate != null && this.withdrawCashLog != null;
    }
}
