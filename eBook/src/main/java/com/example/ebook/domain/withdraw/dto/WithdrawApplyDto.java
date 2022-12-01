package com.example.ebook.domain.withdraw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawApplyDto {

    private Long id;

    private String bankName;
    private String bankAccountNo;
    private int money;

    private LocalDateTime applyDate;
    private LocalDateTime withdrawDate;
}
