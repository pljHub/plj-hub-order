package com.example.eureka.client.order.global.exception.deliveryRecord;

import com.example.eureka.client.order.global.exception.domainErrorCode.DeliveryRecordErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;
import com.example.eureka.client.order.global.exception.dto.ErrorCode;

public class DeliveryRecordAccessDeniedException extends CustomException {

    public DeliveryRecordAccessDeniedException() {
        super(DeliveryRecordErrorCode.DELIVERY_RECORD_NOT_ACCESS);
    }
}
