package com.example.eureka.client.order.application.dto;

import com.example.eureka.client.order.domain.model.OrderStatus;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class OrderItemDto {

    private UUID supplyId;
    private UUID consumerId;
    private UUID productId;
    private Long price;
    private Long quantity;

    @Builder.Default
    private String orderStatus = String.valueOf(OrderStatus.ORDER_RECEIVED);

}
