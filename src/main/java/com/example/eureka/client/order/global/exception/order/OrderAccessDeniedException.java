package com.example.eureka.client.order.global.exception.order;

import com.example.eureka.client.order.global.exception.domainErrorCode.OrderErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;

public class OrderAccessDeniedException extends CustomException {
    public OrderAccessDeniedException(){
        super(OrderErrorCode.ORDER_NOT_ACCESS);
    }

}
