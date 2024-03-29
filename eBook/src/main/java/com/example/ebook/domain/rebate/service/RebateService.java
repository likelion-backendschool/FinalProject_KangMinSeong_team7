package com.example.ebook.domain.rebate.service;

import com.example.ebook.domain.cash.entity.CashLog;
import com.example.ebook.domain.cash.service.CashLogService;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.order.entity.OrderItem;
import com.example.ebook.domain.order.service.OrderService;
import com.example.ebook.domain.rebate.dto.MakeDataForm;
import com.example.ebook.domain.rebate.dto.RebateOrderItemDto;
import com.example.ebook.domain.rebate.entity.RebateOrderItem;
import com.example.ebook.domain.rebate.exception.CannotRebateItemException;
import com.example.ebook.domain.rebate.exception.RebateItemNotFoundException;
import com.example.ebook.domain.rebate.repository.RebateRepository;
import com.example.ebook.global.mapper.RebateOrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.example.ebook.domain.cash.entity.enumuration.CashLogType.*;
import static java.lang.Integer.parseInt;

@Service
@Transactional
@RequiredArgsConstructor
public class RebateService {


    private final RebateRepository rebateRepository;
    private final OrderService orderService;
    private final CashLogService cashLogService;

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    public void makeRebateData(MakeDataForm makeDataForm) {
        String[] yearAndMonth = makeDataForm.getYearMonth().split("-");

        Calendar cal = Calendar.getInstance();
        cal.set(parseInt(yearAndMonth[0]), parseInt(yearAndMonth[1])-1, 1);

        String fromDate = makeDataForm.getYearMonth() + "-01 00:00:00.000000";
        String toDate = makeDataForm.getYearMonth() + "-%02d 23:59:59.999999".formatted(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        List<OrderItem> orderItems = orderService.findAllByPayDateBetween(
                LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern(DATETIME_FORMAT)));

        List<OrderItem> removeOrderItems = new ArrayList<>();
        List<OrderItem> addOrderItems = new ArrayList<>();

        orderItems.forEach(orderItem -> {
            Optional<RebateOrderItem> rebateOrderItem = rebateRepository.findByOrderItem(orderItem);
            if (rebateOrderItem.isEmpty()) {
                addOrderItems.add(orderItem);
            } else if (!rebateOrderItem.get().isRebateDone()) {
                removeOrderItems.add(orderItem);
                addOrderItems.add(orderItem);
            }
        });

        rebateRepository.removeByOrderItem(removeOrderItems);

        List<RebateOrderItem> rebateOrderItems = addOrderItems.stream()
                .map(RebateOrderItem::new)
                .toList();

        rebateRepository.saveAll(rebateOrderItems);
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAll() {
        return RebateOrderItemMapper.INSTANCE.entitiesToRebateOrderItemDtos(rebateRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<RebateOrderItemDto> findAllByYearAndMonth(String yearMonth) {

        String[] yearAndMonth = yearMonth.split("-");
        String year = yearAndMonth[0];
        String month = yearAndMonth[1];

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, 1);

        String fromDate = year + "-" + month + "-01 00:00:00.000000";
        String toDate = year + "-" + month + "-%02d 23:59:59.999999".formatted(cal.getActualMaximum(Calendar.DAY_OF_MONTH));


        return RebateOrderItemMapper.INSTANCE.entitiesToRebateOrderItemDtos(
                rebateRepository.findAllByPayDateBetweenOrderByIdAsc(
                        LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                        LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern(DATETIME_FORMAT))));
    }

    public void rebateOne(Long rebateOrderItemId) {
        OrderItem orderItem = orderService.findOrderItemById(rebateOrderItemId);

        RebateOrderItem rebateOrderItem = rebateRepository.findByOrderItem(orderItem).orElseThrow(
                () -> new RebateItemNotFoundException("해당 정산 데이터를 찾을 수 없습니다."));

        if (!rebateOrderItem.isRebateAvailable()) {
            throw new CannotRebateItemException("해당 정산 데이터를 정산할 수 없습니다.");
        }

        int rebatePrice = rebateOrderItem.calculateRebatePrice();
        Member seller = rebateOrderItem.getProduct().getMember();

        CashLog cashLog = cashLogService.save(seller, CALCULATE_FROM_SELLER, rebatePrice);
        seller.addRestCash(rebatePrice);
        rebateOrderItem.setRebateDone(cashLog);
    }

    public void rebateAll(String ids) {
        String[] rebateOrderItemIds = ids.split(",");

        for (String rebateOrderItemId : rebateOrderItemIds) {
            rebateOne(Long.valueOf(rebateOrderItemId));
        }
    }
}
