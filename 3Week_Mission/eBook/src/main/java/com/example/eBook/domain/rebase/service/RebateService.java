package com.example.eBook.domain.rebase.service;

import com.example.eBook.domain.rebase.dto.MakeDataForm;
import com.example.eBook.domain.rebase.dto.RebateOrderItemDto;
import com.example.eBook.domain.rebase.repository.RebateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RebateService {


    private final RebateRepository rebateRepository;

    public void save(MakeDataForm makeDataForm) {
        // makeDataForm 통해 rebate 데이터 저장
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAll() {
        // mapStruct 통해 entity -> dto 이후 반환
        return null;
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAllByYear(Integer year) {
        // mapStruct 통해 entity -> dto 이후 반환
        return null;
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAllByYearAndMonth(Integer year, Integer month) {
        // mapStruct 통해 entity -> dto 이후 반환
        return null;
    }

    public void rebateOne(Long rebateOrderItemId) {
        // rebateOrderItemId 엔티티 찾은 후
        // rebate 처리 및 CashLog 처리
    }

    public void rebateAll(String ids) {
        // ids split
        // rebate 처리 및 CashLog 처리
    }
}
