package com.example.eureka.client.order.presentation.request;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchDto {
    private UUID orderId;
    private UUID userId;

    private UUID supplyId;
    private UUID consumerId;
    private String orderStatus;
    private Long quantity;
}
