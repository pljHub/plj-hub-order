package com.example.eureka.client.order.global.dto;

import com.example.eureka.client.order.global.exception.dto.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto extends ResponseDto {

    private ErrorResponseDto(HttpStatus status, ErrorCode errorCode){
        super("error", status);
    }

    // ErrorResponseDto 객체를 생성하고 반환
    public static ErrorResponseDto of(HttpStatus status, ErrorCode errorCode){
        return new ErrorResponseDto(status, errorCode);
    }
}
