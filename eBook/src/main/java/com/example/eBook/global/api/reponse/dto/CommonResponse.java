package com.example.eBook.global.api.reponse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {
    private String resultCode;
    private String msg;
    private boolean success;
    private boolean fail;
}
