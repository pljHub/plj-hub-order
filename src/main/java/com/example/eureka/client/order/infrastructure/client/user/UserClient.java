package com.example.eureka.client.order.infrastructure.client.user;

import com.example.eureka.client.order.global.dto.ResponseDto;
import com.example.eureka.client.order.infrastructure.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "USER-SERVICE",
    configuration = FeignClientConfig.class
)
@Primary
public interface UserClient {

    /*
        FeignClient 호출시 @Login 어노테이션을 사용할 수 없다.
     */
    @GetMapping("/api/users/{id}/internal")
    ResponseEntity<ResponseDto<GetUserResponseDto>> getUserInternal(@PathVariable(name = "id") Long userId);

}
