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
    public ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(UUID productId) {
        log.warn("Fallback triggered for reduceProductStock. Failed to reduce stock for product ID: " + productId);
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(ResponseDto.error("HUB-SERVICE NOT RETURNED"));
    }

    @Override
    public ResponseEntity<ResponseDto<Void>> reduceProductStock(UUID productId, int quantity) {
        log.warn("Fallback triggered for getProduct. Could not retrieve product with ID: " + productId);
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

    @Override
    public ResponseEntity<Void> returnProductStock(UUID productId, int quantity) {
        log.warn("Fallback triggered for returnProductStock. Failed to rollback stock with ID: " + productId);
        return ResponseEntity.badRequest().build();
    }


}
