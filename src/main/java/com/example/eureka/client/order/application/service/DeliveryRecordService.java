package com.example.eureka.client.order.application.service;


import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.domain.model.DeliveryRecord;
import com.example.eureka.client.order.domain.repository.DeliveryRecordRepository;
import com.example.eureka.client.order.global.exception.delivery.DeliveryAccessDeniedException;
import com.example.eureka.client.order.global.exception.delivery.DeliveryNotFoundException;
import com.example.eureka.client.order.global.exception.deliveryRecord.DeliveryRecordAccessDeniedException;
import com.example.eureka.client.order.global.exception.deliveryRecord.DeliveryRecordNotFoundException;
import com.example.eureka.client.order.presentation.request.DeliveryRecordSearchDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryRecordService {

    private final DeliveryRecordRepository deliveryRecordRepository;

    public GetDeliveryRecordResponseDto getDeliveryRecord(Long userId, UUID recordId, String role) {
        DeliveryRecord record = deliveryRecordRepository.findById(recordId)
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryRecordNotFoundException::new);

        if (!isMasterOrHubManager(role)) {
            if (!record.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, recordId = {}", userId, recordId);
                throw new DeliveryRecordAccessDeniedException();
            }
        }

        log.info("배달 정보 단건 조회 성공 userId: {}, recordId: {}", userId, recordId);
        return new GetDeliveryRecordResponseDto(record);
    }

    public Page<GetDeliveryRecordResponseDto> getDeliveriesRecords(DeliveryRecordSearchDto searchDto, Pageable pageable, Long userId, String role) {
        DeliveryRecord record = deliveryRecordRepository.findById(searchDto.getRecordId())
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryRecordNotFoundException::new);

        if (!isMasterOrHubManager(role)) {
            if (!record.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, recordId = {}", userId, searchDto.getRecordId());
                throw new DeliveryRecordAccessDeniedException();
            }
        }

        return deliveryRecordRepository.searchDeliveriesRecords(searchDto, pageable, role, userId);
    }

    private DeliveryRecord findDeliveryRecordById(UUID recordId) {
        return deliveryRecordRepository.findById(recordId).orElseThrow(
            DeliveryRecordNotFoundException::new);
    }

    private boolean isMasterOrHubManager(String role) {
        return "MASTER".equals(role) || "HUB_MANAGER".equals(role);
    }
}
