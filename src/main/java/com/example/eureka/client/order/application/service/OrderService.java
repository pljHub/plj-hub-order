package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.application.dto.CreateOrderResponseDto;
import com.example.eureka.client.order.application.dto.GetOrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderItemDto;
import com.example.eureka.client.order.application.dto.OrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderSearchRequestTimeDto;
import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.domain.model.DeliveryRecord;
import com.example.eureka.client.order.domain.model.Order;
import com.example.eureka.client.order.domain.model.OrderStatus;
import com.example.eureka.client.order.domain.model.ProductOrder;
import com.example.eureka.client.order.domain.repository.DeliveryRepository;
import com.example.eureka.client.order.domain.repository.OrderRepository;
import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.global.exception.order.OrderAccessDeniedException;
import com.example.eureka.client.order.global.exception.order.OrderNotFoundException;
import com.example.eureka.client.order.infrastructure.client.delivery.HubPathSequenceDTO;
import com.example.eureka.client.order.infrastructure.client.product.ProductClient;
import com.example.eureka.client.order.infrastructure.client.product.ProductResponseDto;
import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.example.eureka.client.order.presentation.request.OrderSearchDto;
import com.example.eureka.client.order.presentation.request.ProductDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductClient productClient;

    @Transactional
    public CreateOrderResponseDto createOrder(OrderRequest request, Long userId, String role) {

        Order order = Order.createOrder(request);

        Long totalPrice = 0L;
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        /*
            보상 트랜잭션을 적용한 방식:
            - 상품 재고 감소가 성공하면 트랜잭션을 기록.
            - 재고 감소 중 하나라도 실패하면 성공한 재고 감소 작업에 대해 보상 트랜잭션(재고 복구)을 실행.
         */
        List<UUID> successfulProducts = new ArrayList<>();

        try {
            for (Entry<UUID, ProductDetails> entry : request.getProductMap().entrySet()) {
                UUID productId = entry.getKey();
                ProductDetails productDetails = entry.getValue();

                ResponseEntity<ResponseDto<ProductResponseDto>> response = productClient.getProduct(
                    productId);
                ProductResponseDto product = response.getBody().getData();

                log.info("product_id = {}, product_stock = {}", product.getProductId(),
                    product.getStock());

                if (product.getStock() < productDetails.getQuantity()) {
                    throw new RuntimeException();
                }

                // 재고 감소 처리
                productClient.reduceProductStock(productId,
                    Math.toIntExact(productDetails.getQuantity()));
                successfulProducts.add(productId);

                ProductOrder productOrder = ProductOrder.createProductOrder(productId,
                    productDetails);
                order.addProductOrder(productOrder);

                OrderItemDto orderItemDto = new OrderItemDto(
                    request.getSupplyId(),
                    request.getConsumerId(),
                    productId,
                    productDetails.getPrice(),
                    productDetails.getQuantity(),
                    String.valueOf(OrderStatus.ORDER_RECEIVED)
                );
                orderItemDtoList.add(orderItemDto);

                totalPrice += productDetails.getPrice() * productDetails.getQuantity();
            }

            DeliveryRecord deliveryRecord = DeliveryRecord.createDeliveryRecord(request);
            String routePath = getCombinedRoutePath(request.getStartHubId(),
                request.getDestHubId());
            deliveryRecord.allocateSequence(routePath);

            Delivery delivery = Delivery.createDelivery(request);
            delivery.addDeliveryRecord(deliveryRecord);

            order.addDelivery(delivery);
            order.updateTotalPrice(totalPrice);
            orderRepository.save(order);

        }catch (Exception e){
            // 보상 트랜잭션 (재고 롤백 처리)
            for (UUID productId : successfulProducts) {
                ProductDetails productDetails = request.getProductMap().get(productId);

                // 재고 복구 요청
                try{
                    productClient.returnProductStock(productId, Math.toIntExact(productDetails.getQuantity()));
                    log.info("Successfully rolled back stock for product ID: " + productId);
                }catch (Exception rollbackException){
                    log.warn("Failed to rollback stock for product ID: " + productId, rollbackException);
                }
            }
            throw new RuntimeException("주문 처리 중 오류 발생, 보상 트랜잭션 수행: " + e.getMessage());
        }

        return new CreateOrderResponseDto(order.getId(), totalPrice, orderItemDtoList);
    }

    private String getCombinedRoutePath(UUID startHubId, UUID destHubId) {

        ResponseEntity<ResponseDto<List<HubPathSequenceDTO>>> response = productClient.getHubPathSequence(startHubId, destHubId);

        List<HubPathSequenceDTO> hubPathSequenceList = response.getBody().getData();

        StringBuilder combinedRoutePath = new StringBuilder();

        hubPathSequenceList.forEach(hubPathSequence -> {
            if (combinedRoutePath.length() > 0){
                combinedRoutePath.append(" -> ");
            }
            combinedRoutePath.append(hubPathSequence.getRoutePath());
        });

        return combinedRoutePath.toString();
    }

    @Transactional
    public OrderResponseDto acceptOrder(Long userId, UUID orderId, String role) {

        if (!isMasterOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 주문승낙 요청 실패 loginUserId = {}, orderID = {}", userId, orderId);
            throw new OrderAccessDeniedException();
        }

        Order order = findOrderById(orderId);
        order.acceptOrder();

        log.info("주문 승낙 성공 userId: {}, orderId: {}", userId, orderId);

        return new OrderResponseDto(orderId);
    }

    @Transactional
    public OrderResponseDto rejectOrder(Long userId, UUID orderId, String role) {

        if (!isMasterOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 주문거절 요청 실패 loginUserId = {}, orderID = {}", userId, orderId);
            throw new OrderAccessDeniedException();
        }

        Order order = findOrderById(orderId);
        order.rejectOrder();

        log.info("주문 거절 성공 userId: {}, orderId: {}", userId, orderId);

        return new OrderResponseDto(orderId);
    }

    @Transactional
    public OrderResponseDto completeOrder(Long userId, UUID orderId, String role) {

        if (!isMasterOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 주문거절 요청 실패 loginUserId = {}, orderID = {}", userId, orderId);
            throw new OrderAccessDeniedException();
        }

        Order order = findOrderById(orderId);
        order.completeOrder();

        log.info("주문 처리 성공 userId: {}, orderId: {}", userId, orderId);

        return new OrderResponseDto(orderId);
    }

    @Transactional
    public OrderResponseDto cancelOrder(Long userId, UUID orderId, String role) {

        Order order = findOrderById(orderId);

        if (!isMasterOrHubManager(role)) {
            if (!order.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 주문취소 요청 실패 loginUserId = {}, orderID = {}", userId, orderId);
                throw new OrderAccessDeniedException();
            }
        }

        order.cancelOrder();

        log.info("주문 취소 성공 userId: {}, orderId: {}", userId, orderId);

        return new OrderResponseDto(orderId);
    }

    @Transactional
    public OrderResponseDto deleteOrder(Long userId, UUID orderId, String role) {

        Order order = findOrderById(orderId);

        if (!isMasterOrHubManager(role)) {
            if (!order.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 주문삭제 요청 실패 loginUserId = {}, orderID = {}", userId, orderId);
                throw new OrderAccessDeniedException();
            }
        }

        orderRepository.delete(order);

        log.info("주문 삭제 성공 userId: {}, orderId: {}", userId, orderId);

        return new OrderResponseDto(orderId);
    }

    public GetOrderResponseDto getOrder(Long userId, UUID orderId, String role) {

        Order order = orderRepository.findById(orderId)
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(OrderNotFoundException::new);

        if (!isMasterOrHubManager(role)) {
            if (!order.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 주문 조회 요청 실패 loginUserId = {}, orderID = {}", userId, orderId);
                throw new OrderAccessDeniedException();
            }
        }

        log.info("주문 조회 성공 userId: {}, orderId: {}", userId, orderId);

        return new GetOrderResponseDto(order);
    }

    public Page<GetOrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable, Long userId, String role) {
        Order order = orderRepository.findById(searchDto.getOrderId())
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(OrderNotFoundException::new);

        if (!isMasterOrHubManager(role)) {
            if (!order.getUserId().equals(userId)){
                log.warn("유저의 권한 문제 인한 전체 주문 조회 요청 실패 loginUserId = {}, orderID = {}", userId, searchDto.getOrderId());
                throw new OrderAccessDeniedException();
            }
        }
        return orderRepository.searchOrders(searchDto, pageable, role, userId);
    }

    public Page<GetOrderResponseDto> getOrdersByRequestTime(OrderSearchRequestTimeDto searchDto, Pageable pageable, Long userId, String role) {
        if (!isMasterOrHubManager(role)) {
            log.warn("유저의 권한 문제 인한 전체 주문 조회 요청 실패 loginUserId = {}", userId);
            throw new OrderAccessDeniedException();
        }

        return orderRepository.searchOrdersByRequestTime(searchDto, pageable, role, userId);
    }

    private boolean isMasterOrHubManager(String role) {
        return "ADMIN".equals(role) || "HUB_MANAGER".equals(role);
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }
}
