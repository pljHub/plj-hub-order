package com.example.eureka.client.order.presentation.controller;

import com.example.eureka.client.order.application.dto.CreateOrderResponseDto;
import com.example.eureka.client.order.application.dto.GetOrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderSearchRequestTimeDto;
import com.example.eureka.client.order.application.service.OrderService;
import com.example.eureka.client.order.global.dto.ResponseDto;
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
        userId : 업체 담당자
     */
    @PostMapping
    public ResponseEntity<ResponseDto<CreateOrderResponseDto>> createOrder(
        @RequestBody OrderRequest request,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role){

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseDto.success(
                HttpStatus.CREATED.name(),
                orderService.createOrder(request, userId, role)
            ));
    }

    /*
        주문 수락 - 업체 담당자가 업체 배송 담당자를 Delivery 에 배정
        userId : 업체 담당자
        DTO userId : 업체 배송 담당자
     */
    @PatchMapping("/{orderId}/accept")
    public ResponseEntity<ResponseDto<OrderResponseDto>> acceptOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.acceptOrder(userId, orderId, role)
            ));
    }

    /*
        주문 거절
     */
    @PatchMapping("/{orderId}/reject")
    public ResponseEntity<ResponseDto<OrderResponseDto>> rejectOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.rejectOrder(userId, orderId, role)
            ));
    }

    /*
        주문 처리 완료 - 업체 배송 담당자가 출발 허브로 이동할 때
        userId : 업체 배송 담당자
     */
    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<ResponseDto<OrderResponseDto>> completeOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.completeOrder(userId, orderId, role)
            ));
    }


    /*
        주문 취소
     */
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseDto<OrderResponseDto>> cancelOrder(
        @PathVariable("orderId") UUID id,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.cancelOrder(userId, id, role)
            ));
    }


    /*
        주문 삭제(soft delete)
     */
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ResponseDto<OrderResponseDto>> deleteOrder(
        @PathVariable("orderId") UUID id,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.deleteOrder(userId, id, role)
            ));
    }

    /*
        주문 단건 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDto<GetOrderResponseDto>> getOrder(
        @PathVariable UUID orderId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.getOrder(userId, orderId, role)
            ));
    }

    /*
        주문 조회
        /api/orders?page={int}&size={int}&sort={field},{direction}
     */
    @GetMapping
    public ResponseEntity<ResponseDto<Page<GetOrderResponseDto>>> getOrders(
        @RequestBody OrderSearchDto searchDto,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.getOrders(searchDto,pageable, userId, role)
            ));
    }

    /*
        요청한 시간을 전날 시간으로 기준 잡고 현재시간 까지의 주문 목록을 반환하기
        page, queryDsl
     */
    @GetMapping("/requestTime")
    public ResponseEntity<ResponseDto<Page<GetOrderResponseDto>>> getOrdersByRequestTime(
        @RequestBody OrderSearchRequestTimeDto searchDto,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                orderService.getOrdersByRequestTime(searchDto,pageable, userId, role)
            ));
    }
}
