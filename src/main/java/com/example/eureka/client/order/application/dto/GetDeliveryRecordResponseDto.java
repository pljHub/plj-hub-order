package com.example.eureka.client.order.application.dto;

import com.example.eureka.client.order.domain.model.DeliveryRecord;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetDeliveryRecordResponseDto {

    private UUID recordId;
    private String sequence;
    private String status;
    private UUID startHubId;
    private UUID destHubId; // 목적지 허브(o) 도착지 허브(x)

    private Double estimatedDistance;
    private Duration estimatedDuration;
    private Double actualDistance;
    private Duration actualDuration;


    public GetDeliveryRecordResponseDto(DeliveryRecord record) {
        this.recordId = record.getId();
        this.sequence = record.getSequence();
        this.status = String.valueOf(record.getStatus());
        this.startHubId = record.getStartHubId();
        this.destHubId = record.getStartHubId();

        this.estimatedDistance = record.getEstimatedDistance();
        this.estimatedDuration = record.getEstimatedDuration();

        this.actualDistance = record.getActualDistance();
        this.actualDuration = record.getActualDuration();
    }
}
