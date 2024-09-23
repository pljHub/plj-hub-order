package com.example.eureka.client.order.global.exception.dto;

import com.example.eureka.client.order.global.dto.ResponseDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Status: {}, Class: {}, Code: {}, Message: {}";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<String>> handleRuntimeException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        String message = errorCode.getMessage();
        HttpStatus status = errorCode.getStatus();

//        String exception = e.getClass().getSimpleName();
//        String code = e.getErrorCode().getCode();

        log.error(LOG_FORMAT, HttpStatus.BAD_REQUEST, message);

        return ResponseEntity
            .status(status)
            .body(ResponseDto.error(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Optional<ObjectError> first = e.getBindingResult().getAllErrors().stream().findFirst();
        String message = first.isPresent() ? first.get().getDefaultMessage() : "Validation error occurred";

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.error(message));
    }

}
