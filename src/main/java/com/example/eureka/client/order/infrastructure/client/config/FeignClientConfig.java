package com.example.eureka.client.order.infrastructure.client.config;

import static feign.FeignException.errorStatus;
import static java.lang.String.format;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            requestTemplate.header("SERVER-PORT", serverPort);
        };
    }

    @Bean
    ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {

            final var errorMessage
                = format("FeignClient Error: %d, %s", response.status(), response.reason());

            if (response.status() >= 500 && response.status() <= 504) {
                return new RetryableException(response.status(),
                    errorMessage,
                    response.request().httpMethod(),
                    (Long) null, // retry delay
                    response.request());
            }
            throw errorStatus(methodKey, response);
        };
    }
}
