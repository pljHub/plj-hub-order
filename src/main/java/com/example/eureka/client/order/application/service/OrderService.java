package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.application.dto.CreateOrderResponseDto;
import com.example.eureka.client.order.application.dto.GetOrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderSearchRequestTimeDto;
import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.example.eureka.client.order.presentation.request.OrderSearchDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    public CreateOrderResponseDto createOrder(OrderRequest request, Long userId, String role);

    public OrderResponseDto acceptOrder(Long userId, UUID orderId, String role);

    public OrderResponseDto deleteOrder(Long userId, UUID orderId, String role);

    public GetOrderResponseDto getOrder(Long userId, UUID orderId, String role);

    public Page<GetOrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable, Long userId, String role);

    public Page<GetOrderResponseDto> getOrdersByRequestTime(OrderSearchRequestTimeDto searchDto, Pageable pageable);

}
