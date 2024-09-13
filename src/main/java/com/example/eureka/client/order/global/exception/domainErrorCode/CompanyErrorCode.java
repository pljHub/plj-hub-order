package com.example.eureka.client.order.global.exception.domainErrorCode;

import com.example.eureka.client.order.global.exception.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CompanyErrorCode implements ErrorCode {

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 업체를 찾을 수 없습니다.");

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
