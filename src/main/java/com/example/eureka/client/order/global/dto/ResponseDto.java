package com.example.eureka.client.order.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {

    private String status;
    private String message;
    private T data;

    public static <T> ResponseDto <T> success(String meesage) {
        return new ResponseDto<>("success", meesage, null);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>("success", message, data);
    }

    public static <T> ResponseDto <T> error(String message) {
        return new ResponseDto<>("error", message, null);
    }
}
