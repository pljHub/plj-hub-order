package com.example.eureka.client.order.domain.model;

public enum OrderStatus {
    ORDER_RECEIVED, // 주문 생성
    ORDER_PROCESSING, // 주문 수락
    ORDER_REJECTED,
    ORDER_COMPLETED,
    ORDER_CANCELED,
    ORDER_REFUND_FROM_DELIVERY;
}
