package com.example.eureka.client.order.domain.model;

public enum DeliveryStatus {
    WAITING_AT_HUB,
    IN_HUB_TRANSFER,
    ARRIVED_AT_DESTINATION_HUB,
    OUT_FOR_DELIVERY,
    DELIVERED_TO_RECIPIENT,
    RETURNED
}
