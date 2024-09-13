package com.example.eureka.client.order.presentation.request;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryRequestDto {
    private UUID orderId;
    private Long deliveryUserId; // 업체 배송 담당자 또는 허브 배송 담당자(하나의 허브에 10명이 포함되어 있음)
}
