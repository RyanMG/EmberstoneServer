package com.emberstone.emberstone_tavern.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
public class HttpResponseModel<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> HttpResponseModel<T> empty() {
        return success("success", null);
    }

    public static <T> HttpResponseModel<T> success(String message, T data) {
        return HttpResponseModel.<T>builder()
                .message(message)
                .data(data)
                .success(true)
                .build();
    }

    public static <T> HttpResponseModel<T> error(String message, T data) {
        return HttpResponseModel.<T>builder()
                .message(message)
                .data(data)
                .success(false)
                .build();
    }
}
