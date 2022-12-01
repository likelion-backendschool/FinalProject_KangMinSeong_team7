package com.example.ebook.domain.withdraw.repository;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.withdraw.entity.WithdrawApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawRepository extends JpaRepository<WithdrawApply, Long>, CustomWithdrawRepository {

    List<WithdrawApply> findAllByApplicant(Member member);
}
