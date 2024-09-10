package com.example.eureka.client.order.presentation.controller;

import com.example.eureka.client.order.application.dto.CreateOrderResponseDto;
import com.example.eureka.client.order.application.dto.GetOrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderResponseDto;
import com.example.eureka.client.order.application.service.OrderService;
import com.example.eureka.client.order.global.dto.SuccessResponseDto;
import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.example.eureka.client.order.presentation.request.OrderSearchDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /*
        주문 생성
     */
    @PostMapping
    public ResponseEntity<SuccessResponseDto<CreateOrderResponseDto>> createOrder(
        @RequestBody OrderRequest request,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role){

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SuccessResponseDto.of(
                HttpStatus.CREATED,
                orderService.createOrder(request, userId, role)
            ));
    }

    /*
        주문 수락
     */
    @PatchMapping("/{orderId}/accept")
    public ResponseEntity<SuccessResponseDto<OrderResponseDto>> acceptOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.acceptOrder(userId, orderId, role)
            ));
    }

    /*
        주문 거절
     */
    @PatchMapping("/{orderId}/reject")
    public ResponseEntity<SuccessResponseDto<OrderResponseDto>> rejectOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.rejectOrder(userId, orderId, role)
            ));
    }

    /*
        주문 처리 완료
     */
    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<SuccessResponseDto<OrderResponseDto>> completeOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.completeOrder(userId, orderId, role)
            ));
    }


    /*
        주문 취소
     */
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<SuccessResponseDto<OrderResponseDto>> cancelOrder(
        @PathVariable("orderId") UUID id,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.cancelOrder(userId, id, role)
            ));
    }


    /*
        주문 삭제(soft delete)
     */
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<SuccessResponseDto<OrderResponseDto>> deleteOrder(
        @PathVariable("orderId") UUID id,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.deleteOrder(userId, id, role)
            ));
    }

    /*
        주문 단건 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponseDto<GetOrderResponseDto>> getOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.getOrder(userId, orderId, role)
            ));
    }

    /*
        주문 조회
        /api/orders?page={int}&size={int}&sort={field},{direction}
     */
    @GetMapping
    public ResponseEntity<SuccessResponseDto<Page<GetOrderResponseDto>>> getOrders(
        @RequestBody OrderSearchDto searchDto,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponseDto.of(
                HttpStatus.OK,
                orderService.getOrders(searchDto,pageable, userId, role)
            ));
    }


}
