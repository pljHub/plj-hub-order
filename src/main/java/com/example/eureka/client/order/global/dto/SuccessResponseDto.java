package com.example.eureka.client.order.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponseDto<T> extends ResponseDto {

    private final T data; // 응답 데이터 저장

    private SuccessResponseDto(HttpStatus status, T data) {
        super("success", status);
        this.data = data;
    }

    // SuccessResponseDto 객체를 생성하고 반환
    public static <T> SuccessResponseDto<T> of(HttpStatus status, T data) {
        return new SuccessResponseDto<>(status, data);
    }
}
