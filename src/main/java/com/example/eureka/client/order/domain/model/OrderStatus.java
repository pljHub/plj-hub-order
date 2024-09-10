package com.example.eureka.client.order.domain.model;

public enum OrderStatus {
    ORDER_RECEIVED,
    ORDER_PROCESSING,
    ORDER_REJECTED,
    ORDER_COMPLETED,
    ORDER_CANCELED,
    ORDER_REFUND_FROM_DELIVERY;
}
