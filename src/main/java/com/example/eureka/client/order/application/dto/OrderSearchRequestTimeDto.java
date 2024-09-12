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
        "requestTime": "2023-09-12T10:15:30"
     */

    private LocalDateTime requestTime;
}
