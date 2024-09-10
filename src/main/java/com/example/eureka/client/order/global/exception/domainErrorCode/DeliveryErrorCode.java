package com.example.eureka.client.order.global.exception.domainErrorCode;

import com.example.eureka.client.order.global.exception.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeliveryErrorCode implements ErrorCode {

    DELIVERY_NOT_ACCESS(HttpStatus.UNAUTHORIZED, "해당 배달정보에 접근할 수 없습니다. "),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달 정보를 찾을 수 없습니다."),
        ;
    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
