package com.example.eureka.client.order.application.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderSearchRequestTimeDto {
    /*
        "requestTime 자정 -> 24, 오전 1시 ->1, 오후 1시 -> 13
     */
    private Integer requestTime;
}
