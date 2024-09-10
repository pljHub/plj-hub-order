package com.example.eureka.client.order.application.dto;

import com.example.eureka.client.order.domain.model.Delivery;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetDeliveryResponseDto {
    private UUID deliveryId;
    private String status;
    private List<UUID> startHubId;
    private List<UUID> destHubId; // 목적지 허브(o) 도착지 허브(x)
    private String address;
    private String recipient;
    private String number;

    public GetDeliveryResponseDto(Delivery delivery) {
        this.deliveryId = delivery.getId();
        this.status = String.valueOf(delivery.getStatus());
        this.startHubId = delivery.getStartHubIds();
        this.destHubId = delivery.getDestHubIds();
        this.address = delivery.getAddress();
        this.recipient = delivery.getRecipient();
        this.number = delivery.getNumber();
    }
}
