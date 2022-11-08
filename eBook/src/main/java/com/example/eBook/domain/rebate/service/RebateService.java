package com.example.eBook.domain.rebate.service;

import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.order.service.OrderService;
import com.example.eBook.domain.rebate.dto.MakeDataForm;
import com.example.eBook.domain.rebate.dto.RebateOrderItemDto;
import com.example.eBook.domain.rebate.entity.RebateOrderItem;
import com.example.eBook.domain.rebate.repository.RebateRepository;
import com.example.eBook.global.mapper.RebateOrderItemMapper;
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
        return RebateOrderItemMapper.INSTANCE.entitiesToRebateOrderItemDtos(rebateRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAllByYear(Integer year) {

        String fromDate = year + "-01-01 00:00:00.000000";
        String toDate = year + "-12-31 23:59:59.999999";

        return RebateOrderItemMapper.INSTANCE.entitiesToRebateOrderItemDtos(
                rebateRepository.findAllByPayDateBetweenOrderByIdAsc(
                        LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")),
                        LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"))));
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAllByYearAndMonth(Integer year, Integer month) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);

        String fromDate = year + "-" + month + "-01 00:00:00.000000";
        String toDate = year + "-" + month + "-%02d 23:59:59.999999".formatted(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return RebateOrderItemMapper.INSTANCE.entitiesToRebateOrderItemDtos(
                rebateRepository.findAllByPayDateBetweenOrderByIdAsc(
                        LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")),
                        LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"))));
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
