package com.example.ebook.domain.order.service;

import com.example.ebook.domain.member.service.MemberService;
import com.example.ebook.domain.order.entity.Order;
import com.example.ebook.domain.order.exception.CashNotEnoughException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PayService {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    @Value("${custom.tossPayments.secretKey}")
    private String SECRET_KEY;

    @Transactional(readOnly = true)
    public ResponseEntity<JsonNode> confirmPayments(Map<String, String> data, long id, String username) {
        Order order = orderService.findById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        int needCash = order.getTotalPrice() - Integer.parseInt(data.get("amount"));
        int restCash = memberService.getRestCash(username);

        if (needCash > restCash) {
            throw new CashNotEnoughException("예치금이 부족합니다.");
        }

        HttpEntity<String> request = null;
        try {
            request = new HttpEntity<>(objectMapper.writeValueAsString(data), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ObjectMapper JsonProcessingException");
        }

        ResponseEntity<JsonNode> responseEntity = restTemplate
                .postForEntity("https://api.tosspayments.com/v1/payments/confirm", request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            orderService.orderByTossPayments(username, id, needCash);
        }

        return responseEntity;
    }
}
