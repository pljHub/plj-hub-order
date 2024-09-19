package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.application.dto.DeliveryResponseDto;
import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.domain.model.DeliveryStatus;
import com.example.eureka.client.order.domain.model.Order;
import com.example.eureka.client.order.domain.model.OrderStatus;
import com.example.eureka.client.order.domain.repository.DeliveryRepository;
import com.example.eureka.client.order.domain.repository.OrderRepository;
import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.global.exception.company.CompanyNotFoundException;
import com.example.eureka.client.order.global.exception.delivery.DeliveryAccessDeniedException;
import com.example.eureka.client.order.global.exception.delivery.DeliveryNotFoundException;
import com.example.eureka.client.order.global.exception.order.OrderNotFoundException;
import com.example.eureka.client.order.global.util.PermissionUtil;
import com.example.eureka.client.order.infrastructure.client.product.CompanyResponseDTO;
import com.example.eureka.client.order.infrastructure.client.product.ProductClient;
import com.example.eureka.client.order.infrastructure.client.user.GetUserResponseDto;
import com.example.eureka.client.order.infrastructure.client.user.UserClient;
import com.example.eureka.client.order.presentation.request.DeliveryRequestDto;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
    // TODO : 허브에서 허브로 이동하는 과정의 로직이 빠져있음 뒤늦게.. 꺠달음
    // TASK : 허브에서 허브로 이동할 때 해당 Delivery 의 hubDeliveryUserId를 갱신 API 추가 필요
    // TASK : 갱신하는 과정에서 해당 허브 담당 배송자의 hubId 와 현재 hub 위치가 같은지 검증 필요
    // TASK : Delivery 에 현재위치를 저장하는 필드가 있어야 됨

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final PermissionUtil permissionUtil;

    // userId : hubManager
    @Transactional
    public DeliveryResponseDto createDelivery(UUID deliveryId, DeliveryRequestDto requestDto, Long userId,
        String role) {
        Order order = findOrderById(requestDto.getOrderId());
        Delivery delivery = findDeliveryById(deliveryId);
        ResponseEntity<ResponseDto<GetUserResponseDto>> getUserInternal = userClient.getUserInternal(userId);
        GetUserResponseDto userData = getUserInternal.getBody().getData();

        ResponseEntity<ResponseDto<CompanyResponseDTO>> getCompanyInternal = productClient.findCompanyById(order.getSupplierId());
                CompanyResponseDTO companyData = getCompanyInternal.getBody().getData();

        permissionUtil.validateUserOrHubIdPermission(role, userData, companyData, userId);

        if (order.getStatus().equals(OrderStatus.ORDER_ACCEPTED) &&
            order.getDelivery().getCompanyToHubDeliveryUserId() == null){
            log.info("order.getDeliveryId = {}", order.getDelivery().getId());
            delivery.setCompanyToHubDeliveryUserId(requestDto.getDeliveryUserId());
        }

        return new DeliveryResponseDto(deliveryId);
    }

    // userId : companyToHubDeliveryUserId
    @Transactional
    public DeliveryResponseDto acceptDelivery(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {

        Delivery delivery = findDeliveryById(deliveryId);
        Order order = findOrderById(requestDto.getOrderId());

        permissionUtil.validateUserOrCompanyToHubDeliveryUserPermission(role, delivery, userId);

        order.transitOrder();
        delivery.transitDelivery();
        return new DeliveryResponseDto(deliveryId);
    }

    // userId : companyToHubDeliveryUserId
    @Transactional
    public DeliveryResponseDto arriveStartHub(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        permissionUtil.validateUserOrCompanyToHubDeliveryUserPermission(role, delivery, userId);

        delivery.arriveAtStartHub();
        return new DeliveryResponseDto(deliveryId);
    }

    // userId : hubManager
    @Transactional
    public DeliveryResponseDto assignHubDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        if (delivery.getStatus().equals(DeliveryStatus.ARRIVED_AT_START_HUB) &&
            delivery.getHubDeliveryUserId() == null){
            delivery.setHubDeliveryUserId(requestDto.getDeliveryUserId());
        }

        return new DeliveryResponseDto(deliveryId);
    }


    // userId : hubDeliveryUser
    @Transactional
    public DeliveryResponseDto acceptHubDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {

        Delivery delivery = findDeliveryById(deliveryId);

        permissionUtil.validateUserOrHubDeliveryUserPermission(role, delivery, userId);

        delivery.transitBetweenHub();
        return new DeliveryResponseDto(deliveryId);
    }

    // userId : hubDeliveryuser
    @Transactional
    public DeliveryResponseDto arriveDestHub(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        permissionUtil.validateUserOrHubDeliveryUserPermission(role, delivery, userId);

        delivery.arriveAtDestinationHub();
        return new DeliveryResponseDto(deliveryId);
    }


    // userId : HubManger
    @Transactional
    public DeliveryResponseDto assignHubToCompanyToDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);

        if (delivery.getStatus().equals(DeliveryStatus.ARRIVED_AT_DESTINATION_HUB) &&
            delivery.getHubToCompanyDeliveryUserId() == null){
            delivery.setHubToCompanyDeliveryUserId(requestDto.getDeliveryUserId());
        }

        return new DeliveryResponseDto(deliveryId);
    }

    // userId : hubToCompanyDeliveryUser
    @Transactional
    public DeliveryResponseDto acceptHubToCompanyDeliveryUser(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {

        Delivery delivery = findDeliveryById(deliveryId);

        permissionUtil.validateUserOrHubToCompanyDeliveryUserPermission(role, delivery, userId);

        delivery.dispatchToAddress();
        return new DeliveryResponseDto(deliveryId);
    }

    // userId : hubToCompanyDeliveryUser
    @Transactional
    public DeliveryResponseDto completeDelivery(UUID deliveryId, DeliveryRequestDto requestDto, Long userId, String role) {
        Delivery delivery = findDeliveryById(deliveryId);
        Order order = findOrderById(requestDto.getOrderId());

        permissionUtil.validateUserOrHubToCompanyDeliveryUserPermission(role, delivery, userId);

        order.completeOrder();
        delivery.completeDelivery();
        return new DeliveryResponseDto(deliveryId);
    }

    public GetDeliveryResponseDto getDelivery(Long userId, UUID deliveryId, String role) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryNotFoundException::new);

        // TODO : delivery 에 속한 3명의 user 권한 허용 필요
        if (!isMasterOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, orderID = {}", userId, deliveryId);
            throw new DeliveryAccessDeniedException();
        }

        log.info("배달 정보 단건 조회 성공 userId: {}, deliveryId: {}", userId, deliveryId);
        return new GetDeliveryResponseDto(delivery);
    }

    public Page<GetDeliveryResponseDto> getDeliveries(DeliverySearchDto searchDto, Pageable pageable, Long userId, String role) {
        Delivery delivery = deliveryRepository.findById(searchDto.getDeliveryId())
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(DeliveryNotFoundException::new);

        // TODO : delivery 에 속한 3명의 user 권한 허용 필요
        if (!isMasterOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 배달정보 조회 실패(배송 관리자는 user_id 로 판단) loginUserId = {}, deliveryId = {}", userId, searchDto.getDeliveryId());
            throw new DeliveryAccessDeniedException();
        }

        return deliveryRepository.searchDeliveries(searchDto, pageable, role, userId);
    }

    private boolean isMasterOrHubManager(String role) {
        return "ADMIN".equals(role) || "HUB_MANAGER".equals(role);
    }

    private Delivery findDeliveryById(UUID deliveryId) {
        return deliveryRepository.findByIdAndDeletedAtIsNull(deliveryId).orElseThrow(
            DeliveryNotFoundException::new);
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findByIdAndDeletedAtIsNull(orderId).orElseThrow(
            OrderNotFoundException::new);
    }
}
