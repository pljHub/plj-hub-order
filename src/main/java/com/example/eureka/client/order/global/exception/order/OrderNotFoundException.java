package com.example.eureka.client.order.global.exception.order;

import com.example.eureka.client.order.global.exception.domainErrorCode.OrderErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;

public class OrderNotFoundException extends CustomException {
    public OrderNotFoundException(){
        super(OrderErrorCode.ORDER_NOT_FOUND);
    }
}
