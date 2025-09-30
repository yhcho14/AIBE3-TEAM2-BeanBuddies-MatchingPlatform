package com.back.global.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

public record ApiResponse<T>(
        @NonNull String resultCode,
        @JsonIgnore int statusCode,
        @NonNull String msg,
        @NonNull T data
) {
    public ApiResponse(String resultCode, String msg) {
        this(resultCode, msg, null);
    }

    public ApiResponse(String resultCode, String msg, T data) {
        this(resultCode, Integer.parseInt(resultCode.split("-", 2)[0]), msg, data);
    }
}
