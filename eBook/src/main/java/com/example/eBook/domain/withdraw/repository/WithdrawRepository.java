package com.example.eBook.domain.withdraw.repository;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawRepository extends JpaRepository<WithdrawApply, Long>, CustomWithdrawRepository {

    List<WithdrawApply> findAllByApplicant(Member member);
}
