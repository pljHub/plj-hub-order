package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.presentation.request.DeliveryRecordSearchDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRecordService {

    public GetDeliveryRecordResponseDto getDeliveryRecord(Long userId, UUID recordId, String role);

    public Page<GetDeliveryRecordResponseDto> getDeliveriesRecords(
        DeliveryRecordSearchDto searchDto, Pageable pageable, Long userId, String role);
}