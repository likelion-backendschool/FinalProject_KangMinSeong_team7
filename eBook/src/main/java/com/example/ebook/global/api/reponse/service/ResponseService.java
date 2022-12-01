package com.example.ebook.global.api.reponse.service;

import com.example.ebook.global.api.reponse.dto.FailResponse;
import com.example.ebook.global.api.reponse.dto.SuccessMultiResponse;
import com.example.ebook.global.api.reponse.dto.SuccessSingleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    protected  <T> SuccessSingleResponse<T> getSingleDataResponse(T data, String msg, String resultCode, boolean success, boolean fail) {
        SuccessSingleResponse<T> response = new SuccessSingleResponse<>();
        response.setData(data);
        response.setMsg(msg);
        response.setResultCode(resultCode);
        response.setSuccess(success);
        response.setFail(fail);
        return response;
    }

    protected <T> SuccessMultiResponse<T> getMultiDataResponse(List<T> data, String msg, String resultCode, boolean success, boolean fail) {
        SuccessMultiResponse<T> response = new SuccessMultiResponse<>();
        response.setData(data);
        response.setMsg(msg);
        response.setResultCode(resultCode);
        response.setSuccess(success);
        response.setFail(fail);
        return response;
    }

    public <T> SuccessSingleResponse<T> getSuccessSingleDataResponse(T data) {
        return getSingleDataResponse(data, "성공", "S-1", true, false);
    }

    public <T> SuccessMultiResponse<T> getSuccessSingleDataResponse(List<T> data) {
        return getMultiDataResponse(data, "성공", "S-1", true, false);
    }

    public FailResponse getFailResponse(String msg, String errorCode) {
        FailResponse response = new FailResponse();
        response.setResultCode(errorCode);
        response.setMsg(msg);
        response.setSuccess(false);
        response.setFail(true);
        return response;
    }
}
