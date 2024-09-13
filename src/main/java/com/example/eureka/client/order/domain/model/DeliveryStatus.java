package com.example.eureka.client.order.domain.model;

public enum DeliveryStatus {
    PENDING,
    IN_TRANSIT_TO_HUB,
    ARRIVED_AT_START_HUB,
    IN_TRANSIT_BETWEEN_HUBS,
    ARRIVED_AT_DESTINATION_HUB,
    DELIVERED_TO_RECIPIENT,

    WAITING_AT_HUB,
    IN_HUB_TRANSFER,
    OUT_FOR_DELIVERY,

    RETURNED
}
