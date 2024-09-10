package com.example.eureka.client.order.presentation.request;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRecordSearchDto {
    private UUID recordId;
    private UUID startHubId;
    private UUID destHubId;
    private String status;
}
