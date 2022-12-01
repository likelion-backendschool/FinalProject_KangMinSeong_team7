package com.example.ebook.global.api.reponse.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SuccessMultiResponse<T> extends CommonResponse {
    private List<T> data;
}
