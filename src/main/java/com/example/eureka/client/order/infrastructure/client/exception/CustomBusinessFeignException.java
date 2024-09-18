package com.example.eureka.client.order.infrastructure.client.exception;

public class CustomBusinessFeignException extends RuntimeException{
    public CustomBusinessFeignException(String message){
        super(message);
    }

}
