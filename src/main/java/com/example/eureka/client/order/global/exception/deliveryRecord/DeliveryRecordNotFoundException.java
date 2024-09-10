package com.example.eureka.client.order.global.exception.deliveryRecord;

import com.example.eureka.client.order.global.exception.domainErrorCode.DeliveryRecordErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;
import com.example.eureka.client.order.global.exception.dto.ErrorCode;

public class DeliveryRecordNotFoundException extends CustomException {

    public DeliveryRecordNotFoundException() {
        super(DeliveryRecordErrorCode.DELIVERY_RECORD_NOT_FOUND);
    }
}
