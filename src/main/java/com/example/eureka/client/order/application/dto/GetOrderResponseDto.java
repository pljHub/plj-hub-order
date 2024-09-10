package com.example.eureka.client.order.application.dto;

import com.example.eureka.client.order.domain.model.Order;
import com.example.eureka.client.order.domain.model.ProductOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderResponseDto {
    private UUID orderId;
    private Long totalPrice;
    private UUID supplyId;
    private UUID consumerId;
    private Long userId;
    private String orderStatus;
    private List<ProductOrder> productOrderList = new ArrayList<>();

    public GetOrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.supplyId = order.getSupplierId();
        this.consumerId = order.getConsumerId();
        this.userId = order.getUserId();
        this.orderStatus = String.valueOf(order.getStatus());
        for (ProductOrder productOrder : order.getProductOrderList()) {
            productOrderList.add(productOrder);
        }
    }
}
