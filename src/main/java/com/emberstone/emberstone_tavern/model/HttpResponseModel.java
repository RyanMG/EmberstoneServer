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

    public static <T> HttpResponseModel<T> empty() {
        return success("success");
    }

    public static <T> HttpResponseModel<T> success(String message) {
        return HttpResponseModel.<T>builder()
                .message(message)
                .success(true)
                .build();
    }

    public static <T> HttpResponseModel<T> error(String message) {
        return HttpResponseModel.<T>builder()
                .message(message)
                .success(false)
                .build();
    }
}
