package com.back.global.exception;

import com.back.global.response.ApiResponse;

public class ServiceException extends RuntimeException {
    private final String resultCode;
    private final String msg;

    public ServiceException(String resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public ApiResponse<Void> getApiResponse() {
        return new ApiResponse<>(resultCode, msg, null);
    }
}
