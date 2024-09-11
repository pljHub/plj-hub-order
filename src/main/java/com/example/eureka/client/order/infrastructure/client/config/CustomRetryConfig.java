package com.example.eureka.client.order.infrastructure.client.config;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CustomRetryConfig {

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry.of(RetryConfig.custom()
            .maxAttempts(3)  // 최대 재시도 횟수
            .intervalFunction(IntervalFunction.ofExponentialRandomBackoff(Duration.ofMillis(3000), 2)) // 3,6,12 배수로 재시도 Ducration
            .retryExceptions(FeignException.FeignServerException.class)
            .retryOnException(
                throwable -> !(throwable instanceof FeignException.FeignClientException)
                    && !(throwable instanceof RetryableException))
            .build());
    }
}
