package com.example.eBook.global.api.reponse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessSingleResponse<T> extends CommonResponse {
    private T data;
}
