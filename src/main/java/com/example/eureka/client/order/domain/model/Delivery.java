package com.example.eureka.client.order.domain.model;

import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_delivery")
@SQLDelete(sql = "UPDATE p_delivery SET deleted_at = CURRENT_TIMESTAMP, is_deleted = true WHERE delivery_id = ?")
public class Delivery extends Auditing{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "delivery_id")
    private UUID id;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus status;

    // List<UUID>를 별도의 테이블에 저장
    @ElementCollection
    @CollectionTable(name = "delivery_start_hub_ids", joinColumns = @JoinColumn(name = "delivery_id"))
    @Column(name = "start_hub_id", nullable = false)
    private List<UUID> startHubIds;

    @ElementCollection
    @CollectionTable(name = "delivery_dest_hub_ids", joinColumns = @JoinColumn(name = "delivery_id"))
    @Column(name = "dest_hub_id", nullable = false)
    private List<UUID> destHubIds; // 목적지 허브 ID

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "user_id")
    private Long userId; // 주문 수락시에 배송 담당자 할당

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_record_id")
    private DeliveryRecord deliveryRecord;

    public static Delivery createDelivery(OrderRequest request) {
        return Delivery.builder()
            .status(DeliveryStatus.WAITING_AT_HUB) // 기본 배송 상태로 설정 (예: PENDING)
            .startHubIds(request.getStartHubIds()) // 요청에서 시작 허브 ID 리스트 가져오기
            .destHubIds(request.getDestHubIds())   // 요청에서 목적지 허브 ID 리스트 가져오기
            .address(request.getAddress())           // 요청에서 주소 가져오기
            .recipient(request.getRecipient())       // 요청에서 수령인 정보 가져오기
            .number(request.getNumber())             // 요청에서 연락처 정보 가져오기
            .build();
    }

    @PreRemove
    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
        this.setIsDeleted(true);
        if (this.deliveryRecord != null) {
            this.deliveryRecord.setDeletedAt(LocalDateTime.now());
            this.deliveryRecord.setIsDeleted(true);
        }
    }

    public void addDeliveryRecord(DeliveryRecord deliveryRecord) {
        this.deliveryRecord = deliveryRecord;
    }

    public void transferInHub() {
        this.status = DeliveryStatus.IN_HUB_TRANSFER;
        this.deliveryRecord.setStatus(DeliveryRecordStatus.IN_HUB_TRANSFER);
    }

    public void arriveAtDestinationHub() {
        this.status = DeliveryStatus.ARRIVED_AT_DESTINATION_HUB;
        this.deliveryRecord.setStatus(DeliveryRecordStatus.ARRIVED_AT_DESTINATION_HUB);

    }

    public void completeDelivery() {
        this.status = DeliveryStatus.DELIVERED_TO_RECIPIENT;
        this.deliveryRecord.setStatus(DeliveryRecordStatus.DELIVERED_TO_RECIPIENT);

        this.deliveryRecord.setActualDuration(Duration.between(this.deliveryRecord.getCreatedAt(), this.deliveryRecord.getUpdatedAt()));
        /*
            실제 거리를 구하는 로직은 api 를 활용해야 할듯
         */
    }

    public void processRefundDelivery() {
        this.status = DeliveryStatus.RETURNED;
        this.deliveryRecord.setStatus(DeliveryRecordStatus.RETURNED);

    }
}
