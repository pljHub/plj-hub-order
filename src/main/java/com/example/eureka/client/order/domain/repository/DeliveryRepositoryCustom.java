package com.example.eureka.client.order.domain.repository;

import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepositoryCustom {
    Page<GetDeliveryResponseDto> searchDeliveries(DeliverySearchDto searchDto, Pageable pageable, String role, Long userId);

}
