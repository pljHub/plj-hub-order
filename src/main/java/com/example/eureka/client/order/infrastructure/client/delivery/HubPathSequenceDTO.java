package com.example.eureka.client.order.infrastructure.client.delivery;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HubPathSequenceDTO {

    private UUID startHubId;
    private UUID destHubId;
    private int sequence;
    private String routePath;

}
