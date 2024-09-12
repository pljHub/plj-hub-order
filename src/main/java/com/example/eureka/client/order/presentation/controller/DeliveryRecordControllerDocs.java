package com.example.eureka.client.order.presentation.controller;


import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.presentation.request.DeliveryRecordSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "DeliveryRecord API", description = "배달 기록에 관한 API : 1. 배달 단건조회, 2. 배달 선택(페이징) 조회 ")
public interface DeliveryRecordControllerDocs {

    @Operation(summary = "배달 기록 조회", description = "특정 배송 기록을 조회합니다. 필요 파라미터: recordId(UUID 형식의 기록 ID), 선택적 헤더: X-User-ID(사용자 ID), X-Role(사용자 역할)")
    @Parameters(value = {
        @Parameter(name = "recordId", description = "조회할 배송 기록의 UUID", required = true, in = ParameterIn.PATH),
        @Parameter(name = "X-User-ID", description = "요청을 보낸 사용자의 ID (헤더)", in = ParameterIn.HEADER, required = false),
        @Parameter(name = "X-Role", description = "요청을 보낸 사용자의 역할 (헤더)", in = ParameterIn.HEADER, required = false)
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "배송 기록 조회 성공",
            content = @Content(schema = @Schema(implementation = GetDeliveryRecordResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 ID의 배송 기록을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ResponseDto.class))
        )
    })
    public ResponseEntity<ResponseDto<GetDeliveryRecordResponseDto>> getDeliveryRecord(
        @PathVariable UUID recordId,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    );


    @Operation(
        summary = "배송 기록 목록 조회",
        description = "주어진 검색 조건과 페이지 정보를 기반으로 배송 기록 목록을 조회합니다."
    )
    @Parameters(value = {
        @Parameter(name = "X-User-ID", description = "요청을 보낸 사용자의 ID (헤더)", in = ParameterIn.HEADER, required = false),
        @Parameter(name = "X-Role", description = "요청을 보낸 사용자의 역할 (헤더)", in = ParameterIn.HEADER, required = false)
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "배송 기록 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = ResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "배송 기록을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ResponseDto.class))
        )
    })
    public ResponseEntity<ResponseDto<Page<GetDeliveryRecordResponseDto>>> getDeliveriesRecords(
        @RequestBody DeliveryRecordSearchDto searchDto,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestHeader(value = "X-User-ID", required = false) Long userId,
        @RequestHeader(value = "X-Role", required = false) String role
    );
}
