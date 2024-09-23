package com.example.eureka.client.order.presentation.controller;

import com.example.eureka.client.order.application.dto.DeliveryResponseDto;
import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.application.service.DeliveryService;
import com.example.eureka.client.order.application.service.DeliveryServiceImpl;
import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.presentation.request.DeliveryRequestDto;
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
import org.springframework.web.bind.annotation.PutMapping;
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
        배달 생성 - OrderServiceImpl
        - Hub Manager 가 CompanyToHubDeliveryUser 배정
     */
    @PutMapping("/{deliveryId}/create")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> createDelivery(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.createDelivery(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        배달 수락
        CompanyDeliveryUser가 배송 수락 (
     */
    @PatchMapping("/{deliveryId}/accept")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> acceptDelivery(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.acceptDelivery(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        CompanyDeliveryUser가 출발 허브에 도착 완료
     */
    @PatchMapping("/{deliveryId}/arrive/startHub")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> arriveStartHub(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.arriveStartHub(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        출발지 HubManager가 HubDeliveryUser 배정
     */
    @PutMapping("/{deliveryId}/assign/hubDeliveryUser")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> assignHubDeliveryUser(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.assignHubDeliveryUser(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        HubDeliveryUser 가 배송 수락 또는 거절(보류)
     */
    @PatchMapping("/{deliveryId}/accept/hubDeliveryUser")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> acceptHubDeliveryUser(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.acceptHubDeliveryUser(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        HubDeliveryUser가 도착지 Hub에 도착 완료
     */
    @PatchMapping("/{deliveryId}/arrive/destHub")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> arriveDestHub(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.arriveDestHub(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        도착지 HubManager가 CompanyDeliveryUser 배정
     */
    @PutMapping("/{deliveryId}/assign/hubToCompanyDeliveryUser")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> assignHubToCompanyToDeliveryUser(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.assignHubToCompanyToDeliveryUser(deliveryId, requestDto, userId, role)
            ));
    }

    /*
        hubToCompanyDeliveryUser 가 배송 수락 또는 거절
     */
    @PatchMapping("/{deliveryId}/accept/hubToCompanyDeliveryUser")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> acceptHubToCompanyDeliveryUser(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.acceptHubToCompanyDeliveryUser(deliveryId, requestDto, userId, role)
            ));
    }

    /*
    CompanyDeliveryUser가 배송 완료
     */
    @PutMapping("/{deliveryId}/complete/delivery")
    public ResponseEntity<ResponseDto<DeliveryResponseDto>> completeDelivery(
        @PathVariable UUID deliveryId,
        @RequestBody DeliveryRequestDto requestDto,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryService.completeDelivery(deliveryId, requestDto, userId, role)
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
