package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.application.dto.DeliveryResponseDto;
import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.domain.model.Order;
import com.example.eureka.client.order.domain.repository.DeliveryRepository;
import com.example.eureka.client.order.domain.repository.OrderRepository;
import com.example.eureka.client.order.global.exception.delivery.DeliveryAccessDeniedException;
import com.example.eureka.client.order.global.exception.delivery.DeliveryNotFoundException;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public DeliveryResponseDto transferInHub(UUID deliveryId, Long userId, String role) {

        Delivery delivery = findDeliveryById(deliveryId);

        if (!isMasterOrHubManager(role)) {
            if (!delivery.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 수정 실패 loginUserId = {}, orderID = {}", userId, deliveryId);
                throw new DeliveryAccessDeniedException();
            }
        }

        delivery.transferInHub();

        log.info("배달 상태값 변경 선공(출발 허브에서 배송중) userId: {}, deliveryId: {}", userId, deliveryId);
        return new DeliveryResponseDto(deliveryId);
    }

    @Transactional
    public DeliveryResponseDto arriveAtDestinationHub(UUID deliveryId, Long userId, String role) {

        Delivery delivery = findDeliveryById(deliveryId);

        if (!isMasterOrHubManager(role)) {
            if (!delivery.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 수정 실패 loginUserId = {}, orderID = {}", userId, deliveryId);
                throw new DeliveryAccessDeniedException();
            }
        }

        delivery.arriveAtDestinationHub();

        log.info("배달 상태값 변경 선공(목적지 허브에 도착) userId: {}, deliveryId: {}", userId, deliveryId);
        return new DeliveryResponseDto(deliveryId);
    }

    @Transactional
    public DeliveryResponseDto completeDelivery(UUID deliveryId, Long userId, String role) {

        Delivery delivery = findDeliveryById(deliveryId);

        if (!isMasterOrHubManager(role)) {
            if (!delivery.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 수정 실패 loginUserId = {}, orderID = {}", userId, deliveryId);
                throw new DeliveryAccessDeniedException();
            }
        }

        delivery.completeDelivery();

        log.info("배달 상태값 변경 선공(수령 업체에 도착) userId: {}, deliveryId: {}", userId, deliveryId);
        return new DeliveryResponseDto(deliveryId);
    }

    @Transactional
    public DeliveryResponseDto processRefundDelivery(UUID deliveryId, Long userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);
        Order order = orderRepository.findByDeliveryId(deliveryId);

        if (!isMasterOrHubManager(role)) {
            if (!delivery.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 수정 실패 loginUserId = {}, orderID = {}", userId, deliveryId);
                throw new DeliveryAccessDeniedException();
            }
        }

        order.processRefundFromDelivery();

        log.info("배달 상태값 변경 선공(물품 반품 완료) userId: {}, deliveryId: {}", userId, deliveryId);
        return new DeliveryResponseDto(deliveryId);
    }

    private boolean isMasterOrHubManager(String role) {
        return "ADMIN".equals(role) || "HUB_MANAGER".equals(role);
    }

    public GetDeliveryResponseDto getDelivery(Long userId, UUID deliveryId, String role) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryNotFoundException::new);

        if (!isMasterOrHubManager(role)) {
            if (!delivery.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, orderID = {}", userId, deliveryId);
                throw new DeliveryAccessDeniedException();
            }
        }

        log.info("배달 정보 단건 조회 성공 userId: {}, deliveryId: {}", userId, deliveryId);
        return new GetDeliveryResponseDto(delivery);
    }

    public Page<GetDeliveryResponseDto> getDeliveries(DeliverySearchDto searchDto, Pageable pageable, Long userId, String role) {
        Delivery delivery = deliveryRepository.findById(searchDto.getDeliveryId())
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryNotFoundException::new);

        if (!isMasterOrHubManager(role)) {
            if (!delivery.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, deliveryId = {}", userId, searchDto.getDeliveryId());
                throw new DeliveryAccessDeniedException();
            }
        }

        return deliveryRepository.searchDeliveries(searchDto, pageable, role, userId);
    }

    private Delivery findDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(DeliveryNotFoundException::new);
    }

}
