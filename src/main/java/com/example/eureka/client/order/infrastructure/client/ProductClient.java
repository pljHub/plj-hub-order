package com.example.eureka.client.order.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "HUB-SERVICE")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponseDto getProduct(@PathVariable("id") UUID id);

    @GetMapping("/api/products/{id}/reduceStock")
    void reduceProductStock(@PathVariable("id") UUID id, @RequestParam("stock") Long stock);
}
