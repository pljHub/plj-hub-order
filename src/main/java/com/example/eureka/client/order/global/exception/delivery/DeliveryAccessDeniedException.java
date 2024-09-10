package com.example.eureka.client.order.global.exception.delivery;

import com.example.eureka.client.order.global.exception.domainErrorCode.DeliveryErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;

public class DeliveryAccessDeniedException extends CustomException {
    public DeliveryAccessDeniedException(){
        super(DeliveryErrorCode.DELIVERY_NOT_ACCESS);
    }
}
