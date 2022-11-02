package com.example.eBook.domain.cash.service;

import com.example.eBook.domain.cash.entity.CashLog;
import com.example.eBook.domain.cash.entity.enumuration.CashLogType;
import com.example.eBook.domain.cash.repository.CashLogRepository;
import com.example.eBook.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashLogService {

    private final CashLogRepository cashLogRepository;

    public CashLog save(Member member, CashLogType cashLogType, int price) {
        return cashLogRepository.save(CashLog.builder()
                .member(member)
                .eventType(cashLogType)
                .price(price)
                .build());
    }
}
