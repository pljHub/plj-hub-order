package com.example.eureka.client.order.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto {
    private final String success;      // 요청 성공 여부
    private final HttpStatus status;    // HTTP 상태 코드

    public static ResponseDto of(String success, HttpStatus status) {
        return new ResponseDto(success, status);
    }
}
