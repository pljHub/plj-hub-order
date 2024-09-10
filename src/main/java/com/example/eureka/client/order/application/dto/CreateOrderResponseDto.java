package com.example.eureka.client.order.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponseDto {
    private UUID orderId;
    private Long totalPrice;
    private List<OrderItemDto> orderItemList;
}
