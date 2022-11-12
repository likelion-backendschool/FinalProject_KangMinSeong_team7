package com.example.eBook.domain.withdraw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class WithdrawApplyForm {

    @NotEmpty(message = "은행명을 입력하세요.")
    private String bankName;
    @NotEmpty(message = "계좌번호를 입력하세요.")
    private String bankAccountNo;
    @Min(value = 1000, message = "1000원 이상 출금이 가능합니다.")
    @NotNull(message = "출금할 금액을 입력하세요.")
    private Integer money;
}
