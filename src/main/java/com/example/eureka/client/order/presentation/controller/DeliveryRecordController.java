package com.example.eureka.client.order.presentation.controller;


import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.application.service.DeliveryRecordService;
import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.presentation.request.DeliveryRecordSearchDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries/records")
@RequiredArgsConstructor
public class DeliveryRecordController {

    private final DeliveryRecordService deliveryRecordService;

    /*
        배송 기록 단건 조회
     */
    @GetMapping("/{recordId}")
    public ResponseEntity<ResponseDto<GetDeliveryRecordResponseDto>> getDeliveryRecord(
        @PathVariable UUID recordId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryRecordService.getDeliveryRecord(userId, recordId, role)
            ));
    }


    /*
        배송 기록 선택 조회
        - start_hub_id, dest_hub_id, status
     */
    @GetMapping
    public ResponseEntity<ResponseDto<Page<GetDeliveryRecordResponseDto>>> getDeliveriesRecords(
        @RequestBody DeliveryRecordSearchDto searchDto,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    ){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.success(
                HttpStatus.OK.name(),
                deliveryRecordService.getDeliveriesRecords(searchDto, pageable, userId, role)
            ));
    }

    /*
       배송 실제 경로 및 시간 갱신(배송지에 도착시)
       - DeliveryRecordStatus 값이 DELIVERED_TO_RECIPIENT 다음과 같은때 DeliveryService 계층에서 진행
        - 예상 거리 및 시간 : request 에 입력
        - 실제 시간 : 배송지에 도착후에 updated_at - created_at 을 갱신
        - 실제 거리 : start, dest 실제 주소를 이용하여 거리를 계산
     */

    /*
        배송 경로 기록 생성 - OrderService
        - Order, Delivery, DeliveryRecord 생성 시점 동일

        배송 경로는 최초에 모든 경로가 생성되어야 합니다.
        - sequence : hub_road 테이블 start_hub, dest_hub 일치하는 route_path 가져오기
        - hub_road 테이블과 m : n 으로 이루어져있다.
        - delivery 의 s_id, d_id 와 같다. (1:1)

        이동경로
        - 예상 거리 및 시간 : request 에 입력
        - 실제 시간 : 배송지에 도착후에 updated_at - created_at 을 갱신
        - 실제 거리 : start, dest 실제 주소를 이용하여 거리를 계산
     */

    /*
        배송 기록 Status 값 갱신
        - DeliveryService 계층에서 동시 진행
     */

    /*
        배송 기록 삭제
        - CascadeType.ALL, @PreRemove 로 인한
        - order -> delivery -> deliveryRecord 연쇄 soft Delete
     */
}
