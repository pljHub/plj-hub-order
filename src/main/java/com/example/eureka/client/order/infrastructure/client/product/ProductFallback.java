package com.example.eureka.client.order.infrastructure.client.product;

import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.infrastructure.client.delivery.HubPathSequenceDTO;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductFallback implements ProductClient{

    @Override
    public ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(UUID id) {
        log.warn("Fallback triggered for reduceProductStock. Failed to reduce stock for product ID: " + id);
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(ResponseDto.success(
                String.valueOf(new ProductResponseDto(id, "default", -1L, -1L))));
    }

    @Override
    public ResponseEntity<ResponseDto<Void>> reduceProductStock(UUID id, Long stock) {
        log.warn("Fallback triggered for getProduct. Could not retrieve product with ID: " + id);
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(ResponseDto.error("HUB-SERVICE NOT RETURNED"));
    }

    @Override
    public ResponseEntity<ResponseDto<List<HubPathSequenceDTO>>> getHubPathSequence(UUID startHubId,
        UUID destHubId) {
        log.warn("Fallback triggered for getHubPathSequence. Could not retrieve startHubId, destHubId with ID: " + startHubId  + ", " + destHubId);
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(ResponseDto.error("HUB-SERVICE NOT RETURNED"));
    }
}
