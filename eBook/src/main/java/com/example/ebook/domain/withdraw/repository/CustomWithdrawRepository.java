package com.example.ebook.domain.withdraw.repository;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.withdraw.entity.WithdrawApply;

import java.util.List;

public interface CustomWithdrawRepository {

    List<WithdrawApply> findRecentOneByApplicant(Member applicant);
}
