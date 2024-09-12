package com.example.eureka.client.order.presentation.request;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    /*
        OrderRequest
     */
    private UUID supplyId;
    private UUID consumerId;
    private Long userId; // 업체 배송 담당자
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
}
