package com.example.eureka.client.order.application.dto;

import com.example.eureka.client.order.domain.model.OrderStatus;
import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.example.eureka.client.order.presentation.request.ProductDetails;
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
    private String orderStatus = String.valueOf(OrderStatus.ORDER_CREATED);

    public OrderItemDto(OrderRequest request, UUID productId, ProductDetails productDetails,
        String status) {
        this.supplyId = request.getSupplyId();
        this.consumerId = request.getConsumerId();
        this.productId = productId;
        this.price = productDetails.getPrice();
        this.quantity = productDetails.getQuantity();
        this.orderStatus = status;
    }
}
