package com.example.eureka.client.order.global.exception.domainErrorCode;

import com.example.eureka.client.order.global.exception.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeliveryRecordErrorCode implements ErrorCode {

    DELIVERY_RECORD_NOT_ACCESS(HttpStatus.UNAUTHORIZED, "해당 배달기록 정보에 접근할 수 없습니다. "),
    DELIVERY_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배달기록 정보에 찾을 수 없습니다."),
        ;
    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
