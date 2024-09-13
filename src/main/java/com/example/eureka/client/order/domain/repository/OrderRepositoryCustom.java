package com.example.eureka.client.order.domain.repository;

import com.example.eureka.client.order.application.dto.GetOrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderSearchRequestTimeDto;
import com.example.eureka.client.order.presentation.request.OrderSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<GetOrderResponseDto> searchOrders(OrderSearchDto searchDto, Pageable pageable, String role, Long userId);

    Page<GetOrderResponseDto> searchOrdersByRequestTime(OrderSearchRequestTimeDto searchDto, Pageable pageable);

}
