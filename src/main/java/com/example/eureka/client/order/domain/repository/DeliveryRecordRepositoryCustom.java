package com.example.eureka.client.order.domain.repository;

import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.presentation.request.DeliveryRecordSearchDto;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRecordRepositoryCustom {
    Page<GetDeliveryRecordResponseDto> searchDeliveriesRecords(DeliveryRecordSearchDto searchDto, Pageable pageable, String role, Long userId);

}
