package com.example.eureka.client.order.infrastructure.client.product;

import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.infrastructure.config.FeignClientConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "HUB-SERVICE",
    configuration = FeignClientConfig.class,
    fallbackFactory = ProductFallbackFactory.class
)
@Primary
public interface ProductClient {

    /*
        일치 : Http method, URL, @PathVariable("id")
        불일치 : ResponseEntity 의 Body 값만 일치하면 된다. Method name
     */

    @Retry(name = "productServiceRetry")
    @CircuitBreaker(name = "productServiceCircuitBreaker")
    @GetMapping("/api/products/{productId}")
    ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(@PathVariable UUID productId);

    @PutMapping("/api/products/{productId}/reduceStock/internal")
    ResponseEntity<ResponseDto<Void>> reduceProductStock(@PathVariable UUID productId, @RequestParam int quantity);

    @GetMapping("/api/hub-path/sequence")
    ResponseEntity<ResponseDto<List<HubPathSequenceDTO>>> getHubPathSequence(
        @RequestParam("startHubId") UUID startHubId,
        @RequestParam("destHubId") UUID destHubId
    );

    @PutMapping("/api/products/{productId}/returnStock/internal")
    ResponseEntity<Void> returnProductStock(@PathVariable UUID productId, @RequestParam int quantity);

    @GetMapping("/api/companies/{companyId}")
    ResponseEntity<ResponseDto<CompanyResponseDTO>> findCompanyById(@PathVariable UUID companyId);

}
