package com.example.eureka.client.order.domain.repository;

import com.example.eureka.client.order.domain.model.DeliveryRecord;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRecordRepository extends JpaRepository<DeliveryRecord, UUID>, DeliveryRecordRepositoryCustom {

}
