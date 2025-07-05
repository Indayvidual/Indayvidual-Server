package com.indayvidual.server.global.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.indayvidual.server.global.api.code.BaseCode;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 성공 응답
    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), data);
    }

    public static ApiResponse<Void> onSuccess(String code, String message) {
        return new ApiResponse<>(true, code, message, null);
    }

    public static <T> ApiResponse<T> onSuccess(T data, String code, String message) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T data) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), data);
    }


    // 실패 응답
    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
