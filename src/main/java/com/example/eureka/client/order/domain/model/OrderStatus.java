package com.example.eureka.client.order.domain.model;

public enum OrderStatus {
    PENDING,
    ORDER_CREATED,
    ORDER_ACCEPTED,
    ORDER_IN_TRANSIT,
    ORDER_COMPLETED,


    ORDER_PROGRESSING,
    ORDER_REJECTED,
    ORDER_CANCELED,
    ORDER_REFUND_FROM_DELIVERY;
}
