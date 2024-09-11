package com.example.eureka.client.order.infrastructure.client.product;

import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.infrastructure.client.config.FeignClientConfig;
import com.example.eureka.client.order.infrastructure.client.delivery.HubPathSequenceDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "HUB-SERVICE",
//    configuration = FeignClientConfig.class,
    fallbackFactory = ProductFallbackFactory.class
)
@Primary
public interface ProductClient {

    @Retry(name = "productServiceRetry")
    @CircuitBreaker(name = "productServiceCircuitBreaker")
    @GetMapping("/api/products/{id}")
    ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(@PathVariable("id") UUID id);

    @Retry(name = "productServiceRetry")
    @CircuitBreaker(name = "productServiceCircuitBreaker")
    @GetMapping("/api/products/{id}/reduceStock")
    ResponseEntity<ResponseDto<Void>> reduceProductStock(@PathVariable("id") UUID id, @RequestParam("stock") Long stock);

    @Retry(name = "productServiceRetry")
    @CircuitBreaker(name = "productServiceCircuitBreaker")
    @GetMapping("/api/hub-path/sequence")
    ResponseEntity<ResponseDto<List<HubPathSequenceDTO>>> getHubPathSequence(
        @RequestParam("startHubId") UUID startHubId,
        @RequestParam("destHubId") UUID destHubId
    );
}
