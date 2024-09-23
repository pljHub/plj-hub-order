package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.application.dto.DeliveryResponseDto;
import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.presentation.request.DeliveryRequestDto;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {

    DeliveryResponseDto createDelivery(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto acceptDelivery(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto arriveStartHub(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto assignHubDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto acceptHubDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto arriveDestHub(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto assignHubToCompanyToDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto acceptHubToCompanyDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    DeliveryResponseDto completeDelivery(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role);

    GetDeliveryResponseDto getDelivery(Long userId, UUID deliveryId, String role);

    Page<GetDeliveryResponseDto> getDeliveries(DeliverySearchDto searchDto, Pageable pageable, Long userId, String role);

}
