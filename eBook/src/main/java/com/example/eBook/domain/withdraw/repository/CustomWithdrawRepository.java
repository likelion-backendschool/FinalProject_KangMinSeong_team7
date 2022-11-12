package com.example.eBook.domain.withdraw.repository;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;

import java.util.List;

public interface CustomWithdrawRepository {

    List<WithdrawApply> findRecentOneByApplicant(Member applicant);
}
