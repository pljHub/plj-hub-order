package com.example.eureka.client.order.presentation.request;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderRequest {

    /*
        OrderRequest
     */
    private UUID supplyId;
    private UUID consumerId;
    private Map<UUID, ProductDetails> productMap = new HashMap<>();

    /*
        DeliveryRequest
     */
    private List<UUID> startHubIds;
    private List<UUID> destHubIds;
    private String address;
    private String recipient;
    private String number;

    /*
        DeliveryRecordRequest
     */
    private Double estimatedDistance;
    private Duration estimatedDuration;
    private UUID startHubId;
    private UUID destHubId;

    private UUID companyId; // 주문 받을 업체 ID
}
