package com.example.eureka.client.order.presentation.controller;

import com.example.eureka.client.order.application.dto.DeliveryResponseDto;
import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.application.service.DeliveryService;
import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    /*
        배달 생성 - OrderService
        - Order, Delivery, DeliveryRecord 생성 시점 동일
     */

    /*
       출발 허브 도착 후 목적지 허브로 이동중
       - 이전 상태값 확인하는 로직 필요
     */
    @PatchMapping("/{deliveryId}/transfer")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> transferInHub(
        @PathVariable UUID deliveryId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.transferInHub(deliveryId, userId, role)
            ));
    }

    /*
        목적지 허브 도착
     */
    @PatchMapping("/{deliveryId}/arrive")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> arriveAtDestinationHub(
        @PathVariable UUID deliveryId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.arriveAtDestinationHub(deliveryId, userId, role)
            ));
    }

    /*
        요청업체 배달 완료
     */
    @PatchMapping("/{deliveryId}/complete")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> completeDelivery(
        @PathVariable UUID deliveryId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.completeDelivery(deliveryId, userId, role)
            ));
    }

    /*
        요청업체의 반품
     */
    @PatchMapping("/{deliveryId}/refund")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> processRefundDelivery(
        @PathVariable UUID deliveryId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.processRefundDelivery(deliveryId, userId, role)
            ));
    }


    /*
        배달 단건 조회(진행, 완료)
     */
    @GetMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<GetDeliveryResponseDto>> getDelivery(
        @PathVariable UUID deliveryId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.getDelivery(userId, deliveryId, role)
            ));
    }

    /*
        배달 선택 조회(start_hub, dest_hub, address, status)
        /api/deliveries?page={int}&size={int}&sort={field},{direction}

     */
    @GetMapping
    public ResponseEntity<ResponseDto<Page<GetDeliveryResponseDto>>> getDeliveries(
        @RequestBody DeliverySearchDto searchDto,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.getDeliveries(searchDto, pageable, userId, role)
            ));
    }


    /*
        배달 삭제(order - delivery - deliveryRecord : 연쇄 Soft delete)
     */

}
