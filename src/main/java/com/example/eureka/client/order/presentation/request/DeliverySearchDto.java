package com.example.eureka.client.order.presentation.request;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class DeliverySearchDto {
    private UUID deliveryId;
    private UUID startHubId;
    private UUID destHubId;
    private String status;
}
