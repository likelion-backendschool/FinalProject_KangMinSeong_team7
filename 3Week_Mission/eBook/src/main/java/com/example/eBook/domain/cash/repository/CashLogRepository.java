package com.example.eBook.domain.cash.repository;

import com.example.eBook.domain.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
