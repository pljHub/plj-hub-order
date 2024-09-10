package com.example.eureka.client.order.global.exception.delivery;

import com.example.eureka.client.order.global.exception.domainErrorCode.DeliveryErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;

public class DeliveryNotFoundException extends CustomException {
    public DeliveryNotFoundException(){
        super(DeliveryErrorCode.DELIVERY_NOT_FOUND);
    }

}
