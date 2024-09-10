package com.example.eureka.client.order.domain.model;

import com.example.eureka.client.order.presentation.request.OrderRequest;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

@Setter // 편의성
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_delivery_record")
@SQLDelete(sql = "UPDATE p_delivery_record SET deleted_at = CURRENT_TIMESTAMP, is_deleted = true WHERE delivery_record_id = ?")
public class DeliveryRecord extends Auditing{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "delivery_record_id")
    private UUID id;

    @Column(name = "sequence")
    private String sequence;

    @Column(name = "start_hub_id", nullable = false)
    private UUID startHubId;

    @Column(name = "dest_hub_id", nullable = false)
    private UUID destHubId; // 도착 허브 ID

    @Column(name = "estimated_distance", nullable = false)
    private Double estimatedDistance;

    @Column(name = "estimated_duration", nullable = false)
    private Duration estimatedDuration;

    @Column(name = "actual_distance")
    private Double actualDistance;

    @Column(name = "actual_duration")
    private Duration actualDuration;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DeliveryRecordStatus status;

    public static DeliveryRecord createDeliveryRecord(OrderRequest request) {
        return DeliveryRecord.builder()
            .startHubId(request.getStartHubId())
            .destHubId(request.getDestHubId())
            .estimatedDistance(request.getEstimatedDistance())
            .estimatedDuration(request.getEstimatedDuration())
            .status(DeliveryRecordStatus.WAITING_AT_HUB)
            .userId(request.getUserId())
            .build();
    }
}
