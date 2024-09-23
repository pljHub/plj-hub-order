package com.example.eureka.client.order.application.service;


import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.domain.model.DeliveryRecord;
import com.example.eureka.client.order.domain.repository.DeliveryRecordRepository;
import com.example.eureka.client.order.global.exception.deliveryRecord.DeliveryRecordAccessDeniedException;
import com.example.eureka.client.order.global.exception.deliveryRecord.DeliveryRecordNotFoundException;
import com.example.eureka.client.order.global.util.PermissionUtil;
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
public class DeliveryRecordServiceImpl implements DeliveryRecordService{

    private final DeliveryRecordRepository deliveryRecordRepository;
    private final PermissionUtil permissionUtil;

    @Override
    public GetDeliveryRecordResponseDto getDeliveryRecord(Long userId, UUID recordId, String role) {
        DeliveryRecord record = deliveryRecordRepository.findById(recordId)
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryRecordNotFoundException::new);

        if (!permissionUtil.isAdminOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, recordId = {}", userId, recordId);
            throw new DeliveryRecordAccessDeniedException();
        }

        log.info("배달 정보 단건 조회 성공 userId: {}, recordId: {}", userId, recordId);
        return new GetDeliveryRecordResponseDto(record);
    }

    @Override
    public Page<GetDeliveryRecordResponseDto> getDeliveriesRecords(DeliveryRecordSearchDto searchDto, Pageable pageable, Long userId, String role) {
        DeliveryRecord record = deliveryRecordRepository.findById(searchDto.getRecordId())
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryRecordNotFoundException::new);

        if (!permissionUtil.isAdminOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, recordId = {}", userId, searchDto.getRecordId());
            throw new DeliveryRecordAccessDeniedException();
        }

        return deliveryRecordRepository.searchDeliveriesRecords(searchDto, pageable, role, userId);
    }
}
