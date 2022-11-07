package com.example.eBook.domain.rebate.service;

import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.order.service.OrderService;
import com.example.eBook.domain.rebate.dto.MakeDataForm;
import com.example.eBook.domain.rebate.dto.RebateOrderItemDto;
import com.example.eBook.domain.rebate.entity.RebateOrderItem;
import com.example.eBook.domain.rebate.repository.RebateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
@Transactional
@RequiredArgsConstructor
public class RebateService {


    private final RebateRepository rebateRepository;
    private final OrderService orderService;

    public void makeRebateData(MakeDataForm makeDataForm) {
        String[] yearAndMonth = makeDataForm.getYearMonth().split("-");

        Calendar cal = Calendar.getInstance();
        cal.set(parseInt(yearAndMonth[0]), parseInt(yearAndMonth[1])-1, 1);

        String fromDate = makeDataForm.getYearMonth() + "-01 00:00:00.000000";
        String toDate = makeDataForm.getYearMonth() + "-%02d 23:59:59.999999".formatted(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        List<OrderItem> orderItems = orderService.findAllByPayDateBetween(
                LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")),
                LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));

        List<OrderItem> removeOrderItems = orderItems.stream()
                .filter(o -> rebateRepository.findByOrderItem(o).isPresent())
                .toList();

        rebateRepository.removeByOrderItem(removeOrderItems);

        List<RebateOrderItem> rebateOrderItems = orderItems.stream()
                .map(RebateOrderItem::new)
                .toList();

        rebateRepository.saveAll(rebateOrderItems);
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
