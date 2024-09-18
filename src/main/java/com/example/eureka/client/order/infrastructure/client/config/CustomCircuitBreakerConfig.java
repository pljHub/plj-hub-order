package com.example.eureka.client.order.infrastructure.client.config;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.net.ConnectException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CustomCircuitBreakerConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .failureRateThreshold(30)   // 실패 30% 이상 서킷 오픈
            .slowCallDurationThreshold(Duration.ofMillis(9000))    // 9000ms 이상 소요 시 실패로 간주
            .slowCallRateThreshold(30)  // slowCallDurationThreshold 초과 비율이 30% 이상 시 서킷 오픈
            .waitDurationInOpenState(Duration.ofMillis(10000))  // open -> half open 전환 전 대기 시간
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .minimumNumberOfCalls(50)    // 집계에 팔요한 최소 호출 수
            .slidingWindowSize(100)   // 서킷 close 상태에서 n회 호출 도달 시 failureRateThreshold 실패 비율 계산
            .permittedNumberOfCallsInHalfOpenState(20)
            .recordExceptions(FeignException.class, ConnectException.class)
            .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        registry.circuitBreaker("productServiceCircuitBreaker");
        return registry;
    }
}
